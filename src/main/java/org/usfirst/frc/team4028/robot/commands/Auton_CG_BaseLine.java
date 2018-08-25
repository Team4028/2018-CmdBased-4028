package org.usfirst.frc.team4028.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Auton_CG_BaseLine extends CommandGroup
{
    public Auton_CG_BaseLine()
    {
        addSequential(new Chassis_DriveSetDistanceAction(30));
    }
}