package org.usfirst.frc.team4028.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Auton_PIDTune_spinUp extends Command {

	private TalonSRX _talon;
	private ControlMode controlMode;
	private double setpoint;

	public Auton_PIDTune_spinUp(Subsystem requiredSubsystem, TalonSRX talon, ControlMode controlMode,
			double setpoint) {
        _talon = talon;
		this.controlMode = controlMode;
		this.setpoint = setpoint;
		requires(requiredSubsystem);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		_talon.configOpenloopRamp(1.0, 10);
		_talon.set(controlMode, setpoint);
		System.out.println("Starting motor.");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		_talon.set(controlMode.PercentOutput, 0);
	}
}