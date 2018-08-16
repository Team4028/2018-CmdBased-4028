/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4028.robot.commands;

//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.commands.Infeed_RunInfeedWheels;
import org.usfirst.frc.team4028.robot.commands.Infeed_RunInfeedWheels.INFEED_WHEELS_FUNCTION;
import org.usfirst.frc.team4028.robot.commands.Carriage_RunCarriageWheels;
import org.usfirst.frc.team4028.robot.commands.Carriage_RunCarriageWheels.CARRIAGE_WHEELS_FUNCTION;

import edu.wpi.first.wpilibj.command.CommandGroup;
//#endregion

public class CG_InfeedCube extends CommandGroup {
  public CG_InfeedCube() {
    addParallel(new Carriage_RunCarriageWheels(CARRIAGE_WHEELS_FUNCTION.Infeed));
    addSequential(new Infeed_RunInfeedWheels(INFEED_WHEELS_FUNCTION.Infeed));
  }
}
