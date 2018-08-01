package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ZeroElevator extends Command {
	private Elevator _elevator = Elevator.getInstance();
	
    public ZeroElevator() {
        // Use requires() here to declare subsystem dependencies
        requires(_elevator);
        setInterruptible(false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_elevator.initReZeroElevator();
    	setTimeout(5);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	_elevator.zeroElevator();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _elevator.getHasElevatorBeenZeroed() || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
