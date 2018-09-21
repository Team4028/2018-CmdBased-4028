package org.usfirst.frc.team4028.robot.commands;

import java.util.Arrays;



import org.usfirst.frc.team4028.robot.auton.pathfollowing.Paths;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.Paths.Center;
import org.usfirst.frc.team4028.robot.subsystems.Elevator.ELEVATOR_TARGET_POSITION;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


import org.usfirst.frc.team4028.robot.auton.pathfollowing.control.Path;

public class Auton_CG_Switch extends CommandGroup {
	Path toSwitch;
	double elevatorWaitTime;
	
	public Auton_CG_Switch(boolean isSwitchLeft) {
        /*
		if (isSwitchLeft) 
			toSwitch = Paths.getPath(Center.L_SWITCH);
		else
			toSwitch = Paths.getPath(Center.R_SWITCH);

        elevatorWaitTime = 1.0;

        addSequential(new Simultaneous_Command(Arrays.asList(new Command[] {
            new Auton_RunTimedMotionProfileCommand(toSwitch, 3.0),
           
            new Series_Command(Arrays.asList(new Command[] {
                    new WaitCommand(elevatorWaitTime),
                    new Elevator_MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.SWITCH_HEIGHT)
            }))
        })));
        // addSequential( new PrintCommand("YIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEEYIPEE"));
        // Outfeed cube for 0.2s
        addSequential(new CG_OutfeedCube(), 1);
        addSequential(new PrintTimeFromStart());
        // Move Elevator back to floor
        addSequential(new Simultaneous_Command(Arrays.asList(new Command[] {
                new Elevator_MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.INFEED_HEIGHT),
                new Chassis_DriveSetDistanceAction(-20.0)
        })));
        */

        addSequential(new Elevator_MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.SWITCH_HEIGHT));
        
        
	}
	

}