package org.usfirst.frc.team4028.robot.subsystems;

import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.commands.ClimbWithControllers;
import org.usfirst.frc.team4028.robot.util.LogDataBE;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class defines the ClimberServo Subsystem, it is responsible for:
 * 	- Servo
 */
public class ClimberServo extends Subsystem
{
	// define class level working variables
	private Servo _climberServo;
	
	private static final double SERVO_OPEN_POSITION = 1;
	private static final double SERVO_CLOSED_POSITION = 0;
	
	private double _targetServoPosition = 0;
	private static final double SERVO_IN_POSITION_DEADBAND = 0.2;

	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static ClimberServo _instance = new ClimberServo();
	
	public static ClimberServo getInstance() 
	{
		return _instance;
	}
	
	// private constructor for singleton pattern
	private ClimberServo() 
	{
		// Setup Carriage Servo Motors
		_climberServo = new Servo(RobotMap.CLIMBER_SERVO_PWM_ADDRESS);
		
		// set default position
		_targetServoPosition = SERVO_CLOSED_POSITION;
		_climberServo.set(_targetServoPosition);
	}
	
	public void openServo()
	{
		_targetServoPosition = SERVO_OPEN_POSITION;
		_climberServo.set(_targetServoPosition);
	}
	
	public void closeServo()
	{
		_targetServoPosition = SERVO_CLOSED_POSITION;
		_climberServo.set(_targetServoPosition);
	}
	
	public boolean getIsServoInPosition()
	{
		double actualServoPosition = _climberServo.get();
		
		return (Math.abs(actualServoPosition - _targetServoPosition) < SERVO_IN_POSITION_DEADBAND);
	}
	
	public boolean getIsServoCurrentTargetClosed()
	{		
		return (_targetServoPosition == SERVO_CLOSED_POSITION);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }
    
	public void updateLogData(LogDataBE logData) {
	}
	
	public void outputToShuffleboard() {
	}
}