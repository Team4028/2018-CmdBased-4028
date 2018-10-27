package org.usfirst.frc.team4028.robot.commands.auton.autotuning;



import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.computeMean;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.beakCircularBuffer;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4028.robot.subsystems.Chassis;


class Auton_PIDConfig extends Command{




private int _samplesRequired;
private int _samplesGathered = 0;
private int _paramterSlot = 0;
double _desiredVelocity = 0;
private boolean _isFGainBeingTuned;


private TalonSRX _talon;
private StringBuilder _sb;
private beakCircularBuffer _cBuffSpeed;
private beakCircularBuffer _cBuffError;
private TalonSRX[] _slavesList = {};


private computeMean meanComputer = new computeMean();

public Auton_PIDConfig(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot, double desiredVelocity,
        int numSamplesRequired, boolean isFGainBeingTuned) {
    _isFGainBeingTuned = isFGainBeingTuned;
    _talon = talon;
    _samplesRequired = numSamplesRequired;
    _samplesGathered = 0;
    _cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    _cBuffError = new beakCircularBuffer(_samplesRequired);
    _paramterSlot = srxParameterSlot;
    _sb = new StringBuilder();
    _desiredVelocity = desiredVelocity;
    requires(requiredSubsystem);
}

public Auton_PIDConfig(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot, double desiredVelocity,
        int numSamplesRequired, boolean isFGainBeingTuned, TalonSRX[] slaveTalons) {
    _isFGainBeingTuned = isFGainBeingTuned;
    _talon = talon;
    _samplesRequired = numSamplesRequired;
    _samplesGathered = 0;
    _cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    _cBuffError = new beakCircularBuffer(_samplesRequired);
    _paramterSlot = srxParameterSlot;
    _sb = new StringBuilder();
    _desiredVelocity = desiredVelocity;
    _slavesList = slaveTalons;
    requires(requiredSubsystem);
}

// Called just before this Command runs the first time
protected void initialize() {
    System.out.println("Gathering Data...");
}

// Called repeatedly when this Command is scheduled to run
protected void execute() {
    double outputSignal = _talon.getMotorOutputVoltage() / _talon.getBusVoltage();

    double closedLoopError = Math.PI;

    if (_isFGainBeingTuned){
        double speed = _talon.getSelectedSensorVelocity(_paramterSlot);
        _cBuffSpeed.addLast(speed);
    } else {

        _cBuffError.addLast(Math.abs((double)_talon.getClosedLoopError(_paramterSlot)));
    
    }

    _samplesGathered++;
    
    /*
    _sb.append("\tOutput: ");
    _sb.append(outputSignal);
    _sb.append("\tSpeed: ");
    _sb.append(speed);
    _sb.append("\n");
    */
    

    if (_samplesGathered % 2 == 0) {
         System.out.println("Closed loop error: " + _talon.getClosedLoopError(_paramterSlot));
     }
}

// Make this return true when this Command no longer needs to run execute()
protected boolean isFinished() {
    return _samplesGathered >= _samplesRequired;
}

// Called once after isFinished returns true
protected void end() {
    if (_isFGainBeingTuned){
        double kF =  1023/ meanComputer.mean(_cBuffSpeed.toArray());
        _talon.config_kF(_paramterSlot, kF, 10);
        System.out.println("Calculated F gain = " + kF);
        for (TalonSRX slave : _slavesList){
            slave.config_kF(_paramterSlot, kF, 10);
        }
    } else {
        double kP = .1*1023/meanComputer.mean(_cBuffError.toArray());
        _talon.config_kP(_paramterSlot, kP, 10);
        System.out.println("Calculated P Gain = " + kP);
        for (TalonSRX slave : _slavesList){
            slave.config_kF(_paramterSlot, kP, 10);
        }
    }



}

// Called when another command which requires one or more of the same
// subsystems is scheduled to run
protected void interrupted() {
    _talon.set(ControlMode.PercentOutput, 0);
}

}
