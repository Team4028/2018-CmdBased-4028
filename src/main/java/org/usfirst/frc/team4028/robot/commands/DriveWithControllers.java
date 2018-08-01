package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.OI;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;
import org.usfirst.frc.team4028.robot.subsystems.Elevator;
import org.usfirst.frc.team4028.robot.util.ThresholdHandler;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command implements support for driving the chassis
 * 	FYI: This is the default command for for Chassis Subsystem
 */
public class DriveWithControllers extends Command 
{
	private Chassis _chassis = Chassis.getInstance();
	private Elevator _elevator = Elevator.getInstance();
	private OI _oi = OI.getInstance();
	
    public DriveWithControllers() 
    {
        requires(_chassis);
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
    	// optionally throttle speed if elevator is up to prevent tipping
		if (_elevator.isElevatorAtUnsafeHeight()) {
			_chassis.arcadeDrive(0.5 * _oi.getDriver_Throttle_JoystickCmd(), 0.8 * _oi.getDriver_Turn_JoystickCmd());
		} 
		else {
			_chassis.arcadeDrive(_oi.getDriver_Throttle_JoystickCmd(), _oi.getDriver_Turn_JoystickCmd());
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
