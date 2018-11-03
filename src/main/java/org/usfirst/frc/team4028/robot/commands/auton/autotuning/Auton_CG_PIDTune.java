package org.usfirst.frc.team4028.robot.commands.auton.autotuning;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team4028.robot.commands.PrintCommand;
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
			double desiredVelocity, int numSamplesRequired, int numSamplesToCheck, TalonSRX[] listOTalons) {

        addSequential(new WaitCommand("safety_wait_command", 1.0));

        addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.PercentOutput, 1));
        
        addSequential(new PrintCommand("Spin Up Complete"));

        addSequential(new WaitCommand("spin_up_wait_command", 2.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, Auton_PIDConfig.VARIABLE_TUNING.F_GAIN, numSamplesRequired, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new WaitCommand(1.0));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, Chassis.getInstance()._maxSpeedForPIDTuning));

		addSequential(new WaitCommand("spin_up_wait_command", 1.5));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, Chassis.getInstance()._maxSpeedForPIDTuning, numSamplesRequired, Auton_PIDConfig.VARIABLE_TUNING.P_GAIN, numSamplesToCheck, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_finshUp(requiredSubsystem, talon, srxParameterSlot));

	}

}