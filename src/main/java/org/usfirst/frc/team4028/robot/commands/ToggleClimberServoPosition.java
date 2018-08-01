package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.subsystems.ClimberServo;
import org.usfirst.frc.team4028.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command implements support for toggling the position of the Climber Servo
 */
public class ToggleClimberServoPosition extends Command 
{
	private ClimberServo _climberServo = ClimberServo.getInstance();
	private Elevator _elevator = Elevator.getInstance();
	
    public ToggleClimberServoPosition() {
        // Use requires() here to declare subsystem dependencies
        requires(_climberServo);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
    	setTimeout(4);  // set 4 second timeout
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {    	
    	
    	if(_climberServo.getIsServoCurrentTargetClosed() && _elevator.isClimberServoEnabledHeight())
    	{
    		_climberServo.openServo();
    	}
    	else if(_climberServo.getIsServoCurrentTargetClosed() && !_elevator.isClimberServoEnabledHeight()) {
    		DriverStation.reportWarning("Here", true);
    	}
    	else
    	{
    		_climberServo.closeServo();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() 
    {
    	// either we get to target position or we timed out
   	 	return _climberServo.getIsServoInPosition() || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
