package org.usfirst.frc.team4028.robot.commands;



import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.computeMean;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.util.beakCircularBuffer;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4028.robot.subsystems.Chassis;


class Auton_PIDConfig extends Command{


public enum VARIABLE_TUNING
{
    F_GAIN,
    P_GAIN 
}

private int _samplesRequired;
private int _samplesGathered = 0;
private int _paramterSlot = 0;
double _desiredVelocity = 0;
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java

=======
private boolean _fQ;
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java


private TalonSRX _talon;
private StringBuilder _sb;
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java
private beakCircularBuffer _cBuffSpeed;
private beakCircularBuffer _cBuffError;
private int _samplesToCheck;
private int _firstSampleCollectedInd;
private int _lastSamplesCollectedInd;
=======
private beakCircularBuffer cBuffSpeed;
private beakCircularBuffer cBuffError;
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java
private TalonSRX[] _slavesList = {};
private VARIABLE_TUNING _variableTuning;


private computeMean meanComputer = new computeMean();

public Auton_PIDConfig(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot, double desiredVelocity,
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java
        int numSamplesRequired, VARIABLE_TUNING variableTuning, int samplesToCheck) {
    _variableTuning = variableTuning;
    _talon = talon;
    _samplesRequired = numSamplesRequired;
    _samplesGathered = 0;
    _cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    _cBuffError = new beakCircularBuffer(_samplesRequired);
    _paramterSlot = srxParameterSlot;
    _sb = new StringBuilder();
    _desiredVelocity = desiredVelocity;
    _samplesToCheck = samplesToCheck;
    _firstSampleCollectedInd = (int)Math.floor((_samplesToCheck - _samplesRequired) /2);
    _lastSamplesCollectedInd = (int)Math.floor((_samplesToCheck + _samplesRequired)/2);
=======
        int numSamplesRequired, boolean isF) {
    this._fQ = isF;
    this._talon = talon;
    this._samplesRequired = numSamplesRequired;
    this._samplesGathered = 0;
    this.cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    this.cBuffError = new beakCircularBuffer(_samplesRequired);
    this._paramterSlot = srxParameterSlot;
    this._sb = new StringBuilder();
    this._desiredVelocity = desiredVelocity;
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java
    requires(requiredSubsystem);

}

public Auton_PIDConfig(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot, double desiredVelocity,
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java
        int numSamplesRequired,   VARIABLE_TUNING variableTuning ,int samplesToCheck, TalonSRX[] slaveTalons) {
    _variableTuning = variableTuning;
    _talon = talon;
    _samplesRequired = numSamplesRequired;
    _samplesGathered = 0;
    _cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    _cBuffError = new beakCircularBuffer(_samplesRequired);
    _paramterSlot = srxParameterSlot;
    _sb = new StringBuilder();
    _desiredVelocity = desiredVelocity;
    _slavesList = slaveTalons;
=======
        int numSamplesRequired, boolean isF, TalonSRX[] slaveTalons) {
    this._fQ = isF;
    this._talon = talon;
    this._samplesRequired = numSamplesRequired;
    this._samplesGathered = 0;
    this.cBuffSpeed = new beakCircularBuffer(_samplesRequired);
    this.cBuffError = new beakCircularBuffer(_samplesRequired);
    this._paramterSlot = srxParameterSlot;
    this._sb = new StringBuilder();
    this._desiredVelocity = desiredVelocity;
    this._slavesList = slaveTalons;
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java
    requires(requiredSubsystem);
    _samplesToCheck = samplesToCheck;
    _firstSampleCollectedInd = (int)Math.floor((_samplesToCheck - _samplesRequired) /2);
    _lastSamplesCollectedInd = (int)Math.floor((_samplesToCheck + _samplesRequired)/2);

}

// Called just before this Command runs the first time
protected void initialize() {
    System.out.println("Gathering Data...");
}

private boolean checkIfWillKeepSample(){
    switch (_variableTuning){
        case F_GAIN:
            return _samplesGathered != 1;
        case P_GAIN:
            return _samplesGathered >= _firstSampleCollectedInd && _samplesGathered <= _lastSamplesCollectedInd;
    }

    return true;

}

// Called repeatedly when this Command is scheduled to run
protected void execute() {
    double outputSignal = _talon.getMotorOutputVoltage() / _talon.getBusVoltage();

    double closedLoopError = Math.PI;

<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java
    switch (_variableTuning){
        case F_GAIN:
            double speed = _talon.getSelectedSensorVelocity(_paramterSlot);
            if (checkIfWillKeepSample()){
                _cBuffSpeed.addLast(speed);
            }
        case P_GAIN:
            if (checkIfWillKeepSample()){
                _cBuffError.addLast(Math.abs((double)_talon.getClosedLoopError(_paramterSlot)));
            }

=======
    if (_fQ){
        double speed = _talon.getSelectedSensorVelocity(_paramterSlot);
        cBuffSpeed.addLast(speed);
    } else {

        cBuffError.addLast(Math.abs((double)_talon.getClosedLoopError(_paramterSlot)));
    
    }
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java

    }
        
    _samplesGathered++;
    
    /*
    _sb.append("\tOutput: ");
    _sb.append(outputSignal);
    _sb.append("\tSpeed: ");
    _sb.append(speed);
    _sb.append("\n");
    */
    

    if (_samplesGathered % 10 == 0) {
         System.out.println("Closed loop error: " + _talon.getClosedLoopError(_paramterSlot));
     }
}

// Make this return true when this Command no longer needs to run execute()
protected boolean isFinished() {

    boolean isFinished = true;

    switch (_variableTuning){
        case F_GAIN:
            isFinished =  _samplesGathered >= _samplesRequired;
        case P_GAIN:
            isFinished =  _samplesGathered >= _samplesToCheck;
    }

    return isFinished;
   
}


// Called once after isFinished returns true
protected void end() {
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_PIDConfig.java
    switch (_variableTuning){
        case F_GAIN:
            double _maxSpeed = meanComputer.mean(_cBuffSpeed.toArray());
            double kF =  1023/_maxSpeed ;
            _talon.config_kF(_paramterSlot, kF, 10);
            System.out.println("Calculated F gain = " + kF);
            Chassis.getInstance()._maxSpeedForPIDTuning = _maxSpeed;
            for (TalonSRX slave : _slavesList){
                slave.config_kF(_paramterSlot, kF, 10);
            }
        case P_GAIN:
            double kP = .1*1023/meanComputer.mean(_cBuffError.toArray());
            _talon.config_kP(_paramterSlot, kP, 10);
            System.out.println("Calculated P Gain = " + kP);
            for (TalonSRX slave : _slavesList){
                slave.config_kF(_paramterSlot, kP, 10);
    }
   
=======
    if (_fQ){
        double kF =  1023/ meanComputer.mean(cBuffSpeed.toArray());
        _talon.config_kF(_paramterSlot, kF, 10);
        System.out.println("Calculated F gain = " + kF);
        for (TalonSRX slave : _slavesList){
            slave.config_kF(_paramterSlot, kF, 10);
        }
    } else {
        double kP = .1*1023/meanComputer.mean(cBuffError.toArray());
        _talon.config_kP(_paramterSlot, kP, 10);
        System.out.println("Calculated P Gain = " + kP);
        for (TalonSRX slave : _slavesList){
            slave.config_kF(_paramterSlot, kP, 10);
        }
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_PIDConfig.java
    }



}

// Called when another command which requires one or more of the same
// subsystems is scheduled to run
protected void interrupted() {
    _talon.set(ControlMode.PercentOutput, 0);
}

}