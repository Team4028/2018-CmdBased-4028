package org.usfirst.frc.team4028.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class Auton_CG_PIDTune extends CommandGroup {

	public Auton_CG_PIDTune(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot,
			double desiredVelocity, int numSamplesRequired) {

		addSequential(new WaitCommand("safety_wait_command", 1.0));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.PercentOutput, .2));

		addSequential(new WaitCommand("spin_up_wait_command", 2.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, true));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, desiredVelocity));

		addSequential(new WaitCommand("spin_up_wait_command", 5.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, false));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_finshUp(requiredSubsystem, talon, srxParameterSlot));

	}

}