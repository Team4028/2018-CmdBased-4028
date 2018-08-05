/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4028.robot.commands;

//#region Define Imports
import org.usfirst.frc.team4028.robot.subsystems.Carriage;

import edu.wpi.first.wpilibj.command.Command;
//#endregion

public class RunCarriageWheels extends Command {
  public enum CARRIAGE_WHEELS_FUNCTION{
    Infeed, Outfeed,
  }
  private Carriage _carriage = Carriage.getInstance();
  private CARRIAGE_WHEELS_FUNCTION _carriageWheelsFunction;

  public RunCarriageWheels(CARRIAGE_WHEELS_FUNCTION function) {
    requires(_carriage);
    _carriageWheelsFunction = function;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(_carriageWheelsFunction == CARRIAGE_WHEELS_FUNCTION.Infeed){
      _carriage.feedIn();
    }
    else if(_carriageWheelsFunction == CARRIAGE_WHEELS_FUNCTION.Outfeed){
      _carriage.feedOut();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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
