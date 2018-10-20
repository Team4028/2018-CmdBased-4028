package org.usfirst.frc.team4028.robot.commands;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;

public class Auton_CG_ChassisTune extends CommandGroup{

TalonSRX _leftMaster;
TalonSRX _rightMaster;
TalonSRX _leftSlave;
TalonSRX _rightSlave;
Chassis _chassis = Chassis.getInstance();

    public Auton_CG_ChassisTune(){

        _leftMaster = new TalonSRX(RobotMap.LEFT_DRIVE_MASTER_CAN_ADDR);
        _leftSlave = new TalonSRX(RobotMap.LEFT_DRIVE_SLAVE_CAN_ADDR);
        _rightMaster = new TalonSRX(RobotMap.RIGHT_DRIVE_MASTER_CAN_ADDR);
        _rightSlave = new TalonSRX(RobotMap.RIGHT_DRIVE_SLAVE_CAN_ADDR);
    
        _leftSlave.follow(_leftMaster);
        _rightSlave.follow(_leftMaster);
        _rightMaster.follow(_leftMaster);
    
        _leftMaster.setInverted(true);
        _leftSlave.setInverted(true);
        _rightMaster.setInverted(false);
        _rightSlave.setInverted(false);

        TalonSRX[] listOSlaves = {_rightMaster, _rightSlave, _leftSlave};


        addParallel(new Auton_ParallelStarter());
        addSequential(new Auton_CG_PIDTune(_chassis, _leftMaster, RobotMap.LEFT_DRIVE_MASTER_CAN_ADDR, 1500 , 100, listOSlaves ));

        

    }
}