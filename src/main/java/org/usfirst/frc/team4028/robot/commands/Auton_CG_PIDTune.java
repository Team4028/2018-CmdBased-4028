package org.usfirst.frc.team4028.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_CG_PIDTune.java
import org.usfirst.frc.team4028.robot.commands.PrintCommand;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;

=======
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_CG_PIDTune.java
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class Auton_CG_PIDTune extends CommandGroup {
<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_CG_PIDTune.java
=======

	public Auton_CG_PIDTune(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot,
			double desiredVelocity, int numSamplesRequired) {

		addSequential(new WaitCommand("safety_wait_command", 1.0));

		//addSequential(new Chassis_ArcadeDriveAction(1, 2));

        addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.PercentOutput, 1));
        
        addSequential(new PrintCommand("Spin Up Complete"));

		addSequential(new WaitCommand("spin_up_wait_command", 2.0));
		

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, true));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, desiredVelocity));

		addSequential(new WaitCommand("spin_up_wait_command", 5.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, false));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_finshUp(requiredSubsystem, talon, srxParameterSlot));

    }
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_CG_PIDTune.java
    
    	public Auton_CG_PIDTune(Subsystem requiredSubsystem, TalonSRX talon, int srxParameterSlot,
			double desiredVelocity, int numSamplesRequired, int numSamplesToCheck, TalonSRX[] listOTalons) {

        addSequential(new WaitCommand("safety_wait_command", 1.0));

        addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.PercentOutput, 1));
        
        addSequential(new PrintCommand("Spin Up Complete"));

        addSequential(new WaitCommand("spin_up_wait_command", 2.0));

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, desiredVelocity, numSamplesRequired, Auton_PIDConfig.VARIABLE_TUNING.F_GAIN, numSamplesRequired, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

<<<<<<< HEAD:src/main/java/org/usfirst/frc/team4028/robot/commands/auton/autotuning/Auton_CG_PIDTune.java
		addSequential(new WaitCommand(1.0));

		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, Chassis.getInstance()._maxSpeedForPIDTuning));

		addSequential(new WaitCommand("spin_up_wait_command", 1.5));
=======
		addSequential(new Auton_PIDTune_spinUp(requiredSubsystem, talon, ControlMode.Velocity, desiredVelocity));

		addSequential(new WaitCommand("spin_up_wait_command", 3.0));
>>>>>>> parent of 95f1fda... Reorganized Folder Structure within auton commands:src/main/java/org/usfirst/frc/team4028/robot/commands/Auton_CG_PIDTune.java

		addSequential(new Auton_PIDConfig(requiredSubsystem, talon, srxParameterSlot, Chassis.getInstance()._maxSpeedForPIDTuning, numSamplesRequired, Auton_PIDConfig.VARIABLE_TUNING.P_GAIN, numSamplesToCheck, listOTalons));

		addSequential(new Auton_PIDTune_spinDown(requiredSubsystem, talon));

		addSequential(new Auton_PIDTune_finshUp(requiredSubsystem, talon, srxParameterSlot));

	}

}