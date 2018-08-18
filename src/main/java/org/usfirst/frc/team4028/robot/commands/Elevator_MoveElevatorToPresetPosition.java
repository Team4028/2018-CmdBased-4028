package org.usfirst.frc.team4028.robot.commands;

//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.subsystems.Elevator;
import org.usfirst.frc.team4028.robot.subsystems.Elevator.ELEVATOR_TARGET_POSITION;

import edu.wpi.first.wpilibj.command.Command;
//#endregion

public class Elevator_MoveElevatorToPresetPosition extends Command {
	private Elevator _elevator = Elevator.getInstance();
	ELEVATOR_TARGET_POSITION _presetPosition;

    public Elevator_MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION presetPosition) {
        requires(_elevator);
        setInterruptible(true);
        _presetPosition = presetPosition;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(5);  // set 4 second timeout
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	_elevator.MoveToPresetPosition(_presetPosition);    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	 return _elevator.IsAtTargetPosition() || isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
