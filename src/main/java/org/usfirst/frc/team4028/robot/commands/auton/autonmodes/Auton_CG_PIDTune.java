package org.usfirst.frc.team4028.robot.commands.auton.autonmodes;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team4028.robot.Constants;
import org.usfirst.frc.team4028.robot.commands.PrintCommand;
import org.usfirst.frc.team4028.robot.commands.auton.autotuning.Auton_PIDConfig;
import org.usfirst.frc.team4028.robot.commands.auton.autotuning.Auton_PIDTune_finshUp;
import org.usfirst.frc.team4028.robot.commands.auton.autotuning.Auton_PIDTune_spinDown;
import org.usfirst.frc.team4028.robot.commands.auton.autotuning.Auton_PIDTune_spinUp;
import org.usfirst.frc.team4028.robot.commands.auton.autotuning.Auton_PIDConfig.VARIABLE_TUNING;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class Auton_CG_PIDTune extends CommandGroup {


    
	public Auton_CG_PIDTune(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot,
		double desiredVelocity, int numSamplesRequired, int numSamplesToCheck, TalonSRX[] listOTalons)
	{

		addSequential(new WaitCommand("safety_wait_command", 1.0));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.PercentOutput, 1));
		
		addSequential(new PrintCommand("Spin Up Complete"));

		addSequential(new WaitCommand("spin_up_wait_command", 2.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, VARIABLE_TUNING.F_GAIN, numSamplesRequired, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, desiredVelocity));

		addSequential(new WaitCommand("spin_up_wait_command", 3.0));


		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, Constants.MAX_SPEED_HIGH_GEAR, numSamplesRequired, Auton_PIDConfig.VARIABLE_TUNING.P_GAIN, numSamplesToCheck, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_finshUp(requiredSubsystem, talon, srxParameterSlot));

	}

}