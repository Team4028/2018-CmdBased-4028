package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.Constants;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.command.Command;

public class Chassis_DriveSetDistanceAction extends Command
{
	private double _targetDistance;

	Chassis _chassis = Chassis.getInstance();
	public Chassis_DriveSetDistanceAction (double targetDistance)
	{
		requires(_chassis);
		setInterruptible(true);
		_targetDistance = targetDistance;
	}
	@Override
	protected void initialize() 
	{
		_chassis.setMotionMagicCmdInches(_targetDistance);
	}
	@Override
	protected void execute() 
	{
		_chassis.moveToTargetPosDriveSetDistance();
	}
	@Override
	protected boolean isFinished()
	{
		if(Math.abs(_chassis.get_leftPos()-_chassis._leftMtrDriveSetDistanceCmd)<Constants.CHASSIS_DRIVE_SET_DISTANCE_DEADBAND
		&& Math.abs(_chassis.get_rightPos()-_chassis._rightMtrDriveSetDistanceCmd)<Constants.CHASSIS_DRIVE_SET_DISTANCE_DEADBAND)
		{
			
			return true;
		}
		else
		{
			return false;
		}
	}

}