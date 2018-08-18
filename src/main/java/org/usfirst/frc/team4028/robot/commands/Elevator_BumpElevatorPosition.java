package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator_BumpElevatorPosition extends Command {
  public enum ELEVATOR_BUMP_FUNCTION{
    BumpUp, BumpDown,
  }
  Elevator _elevator = Elevator.getInstance();
  ELEVATOR_BUMP_FUNCTION _elevatorBumpFunction;

  public Elevator_BumpElevatorPosition(ELEVATOR_BUMP_FUNCTION function) {
    //requires(_elevator);
    _elevatorBumpFunction = function;
    System.out.println("Heere");
    setTimeout(1);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    System.out.println("Executing");
    if(_elevatorBumpFunction == ELEVATOR_BUMP_FUNCTION.BumpUp){
      _elevator.elevatorPositionBumpUp();
    }
    else if(_elevatorBumpFunction == ELEVATOR_BUMP_FUNCTION.BumpDown){
      _elevator.elevatorPositionBumpUp();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
