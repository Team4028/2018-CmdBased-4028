/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4028.robot.commands;

//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.subsystems.Carriage;

import edu.wpi.first.wpilibj.command.Command;
//#endregion

public class Carriage_ToggleCarriageSolenoids extends Command {
  Carriage _carriage = Carriage.getInstance();
  CARRIAGE_SOLENOID_FUNCTIONS _carriageSolenoidFunction;
  public enum CARRIAGE_SOLENOID_FUNCTIONS{
    FlapUp, FlapDown, Squeeze, Wide,
  }

  public Carriage_ToggleCarriageSolenoids(CARRIAGE_SOLENOID_FUNCTIONS function) {
    requires(_carriage);
    _carriageSolenoidFunction = function;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.FlapUp){
      _carriage.tiltCarriageUp();
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.FlapDown){
      _carriage.tiltCarriageDown();
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.Squeeze){
      _carriage.moveCarriageToSqueezeWidth();
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.Wide){
      _carriage.moveCarriageToWideWidth();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.FlapUp){
      if(_carriage.get_isFlapInUpPosition()){
        return true;
      } else {
        return false;
      }
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.FlapDown){
      if(!_carriage.get_isFlapInUpPosition()){
        return true;
      } else {
        return false;
      }
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.Squeeze){
      if(_carriage.get_isCarriageInSqueezePosition()){
        return true;
      } else {
        return false;
      }
    }
    else if(_carriageSolenoidFunction == CARRIAGE_SOLENOID_FUNCTIONS.Wide){
      if(!_carriage.get_isCarriageInSqueezePosition()){
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
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
