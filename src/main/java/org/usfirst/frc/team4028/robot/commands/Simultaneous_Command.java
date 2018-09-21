package org.usfirst.frc.team4028.robot.commands;

import org.omg.CORBA.PRIVATE_MEMBER;
//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.subsystems.Climber;
import org.usfirst.frc.team4028.robot.subsystems.Elevator;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
//#endregion
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This command implements support for toggling the position of the Climber Servo
 */

public class Simultaneous_Command extends CommandGroup
{
    public Simultaneous_Command(List<Command> commands) {
		for (int i = 0; i< commands.size()-1; i++ ) {
            addParallel(commands.get(i));
		 }
		 addSequential(commands.get(commands.size()-1));
		}

}