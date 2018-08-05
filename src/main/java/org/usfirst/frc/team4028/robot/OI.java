/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4028.robot;

//#region Define Imports
import org.usfirst.frc.team4028.robot.commands.MoveElevatorToPresetPosition;
import org.usfirst.frc.team4028.robot.commands.MoveInfeedArmsToPresetPosition;
import org.usfirst.frc.team4028.robot.commands.RunInfeedWheels;
import org.usfirst.frc.team4028.robot.commands.ZeroInfeedArms;
import org.usfirst.frc.team4028.robot.commands.RunInfeedWheels.INFEED_WHEELS_FUNCTION;
import org.usfirst.frc.team4028.robot.commands.ShiftGear;
import org.usfirst.frc.team4028.robot.commands.ToggleActiveCamera;
import org.usfirst.frc.team4028.robot.commands.ToggleClimberServoPosition;
import org.usfirst.frc.team4028.robot.subsystems.Elevator.ELEVATOR_TARGET_POSITION;
import org.usfirst.frc.team4028.robot.subsystems.Infeed.INFEED_ARM_TARGET_POSITION;
import org.usfirst.frc.team4028.robot.util.TriggerButton;
import org.usfirst.frc.team4028.robot.util.TriggerButton.HAND;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
//#endregion

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//#region CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	//#endregion

	private XboxController DriverController;
	private XboxController OperatorController;
	
	public static final int XBOX_A_PORT = 1;
	public static final int XBOX_B_PORT = 2;
	public static final int XBOX_X_PORT = 3;
	public static final int XBOX_Y_PORT = 4;
	public static final int XBOX_LBUMPER_PORT = 5;
	public static final int XBOX_RBUMPER_PORT = 6; 
	public static final int XBOX_BACK_BUTTON = 7;
	public static final int XBOX_START_BUTTON = 8;
	public static final int XBOX_LSTICK_BUTTON_PORT = 9;  
	public static final int XBOX_RSTICK_BUTTON_PORT = 10;  
	public static final int XBOX_XBOX_PORT = 11;  
	public static final int XBOX_UP_PORT = 12;  
	public static final int XBOX_DOWN_PORT = 13;  
	public static final int XBOX_LEFT_PORT = 14;
	public static final int XBOX_RIGHT_PORT = 15;
	
	public static final int XBOX_LXJOSYSTICK_PORT = 0;
	public static final int XBOX_LYJOYSTICK_PORT = 1;
	public static final int XBOX_LTRIGGER_PORT = 2;
	public static final int XBOX_RTRIGGER_PORT = 3;
	public static final int XBOX_RXJOYSTICK_PORT = 4;
	public static final int XBOX_RYJOYSTICK_PORT = 5;

	public static final double XBOX_AXIS_DEADBAND = 0.1;
	public static final double XBOX_TRIGGER_DEADBAND = 0.02;
	
	public JoystickButton DRIVER_UP;
	public JoystickButton DRIVER_DOWN;
	public JoystickButton DRIVER_LEFT;
	public JoystickButton DRIVER_RIGHT;
	public JoystickButton DRIVER_A;
	public JoystickButton DRIVER_B;
	public JoystickButton DRIVER_X;
	public JoystickButton DRIVER_Y;
	public JoystickButton DRIVER_LBUMPER;
	public JoystickButton DRIVER_RBUMPER;
	public JoystickButton DRIVER_XBOX;
	public JoystickButton DRIVER_LSTICK_BUTTON;
	public JoystickButton DRIVER_RSTICK_BUTTON;
	public JoystickButton DRIVER_START_BUTTON;
	public JoystickButton DRIVER_BACK_BUTTON;
	public TriggerButton DRIVER_LTRIGGER;
	public TriggerButton DRIVER_RTRIGGER;
	
	public JoystickButton OPERATOR_UP;
	public JoystickButton OPERATOR_DOWN;
	public JoystickButton OPERATOR_LEFT;
	public JoystickButton OPERATOR_RIGHT;
	public JoystickButton OPERATOR_A;
	public JoystickButton OPERATOR_B;
	public JoystickButton OPERATOR_X;
	public JoystickButton OPERATOR_Y;
	public JoystickButton OPERATOR_LBUMPER;
	public JoystickButton OPERATOR_RBUMPER;
	public JoystickButton OPERATOR_XBOX;
	public JoystickButton OPERATOR_LSTICK_BUTTON;
	public JoystickButton OPERATOR_RSTICK_BUTTON;
	public JoystickButton OPERATOR_START_BUTTON;
	public JoystickButton OPERATOR_BACK_BUTTON;
	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static OI _instance = new OI();
	
	public static OI getInstance() {
		return _instance;
	}
	
	// private constructor for singleton pattern
	private OI() 
	{	
		// =========== Driver ======================================
		DriverController = new XboxController(RobotMap.DRIVER_GAMEPAD_USB_PORT);
		//==========================================================
		DRIVER_UP = new JoystickButton(DriverController, XBOX_UP_PORT);
		DRIVER_DOWN = new JoystickButton(DriverController, XBOX_DOWN_PORT);
		DRIVER_LEFT = new JoystickButton(DriverController, XBOX_LEFT_PORT);
		DRIVER_RIGHT = new JoystickButton(DriverController, XBOX_RIGHT_PORT);
		
		DRIVER_A = new JoystickButton(DriverController, XBOX_A_PORT);
		DRIVER_B = new JoystickButton(DriverController, XBOX_B_PORT);
		DRIVER_X = new JoystickButton(DriverController, XBOX_X_PORT);
		DRIVER_Y = new JoystickButton(DriverController, XBOX_Y_PORT);
		
		DRIVER_LBUMPER = new JoystickButton(DriverController, XBOX_LBUMPER_PORT);
		DRIVER_RBUMPER = new JoystickButton(DriverController, XBOX_RBUMPER_PORT);
		
		DRIVER_XBOX = new JoystickButton(DriverController, XBOX_XBOX_PORT);
		
		DRIVER_LSTICK_BUTTON = new JoystickButton(DriverController, XBOX_LSTICK_BUTTON_PORT);
		DRIVER_RSTICK_BUTTON = new JoystickButton(DriverController, XBOX_RSTICK_BUTTON_PORT);
		
		DRIVER_START_BUTTON = new JoystickButton(DriverController, XBOX_START_BUTTON);
		DRIVER_BACK_BUTTON = new JoystickButton(DriverController, XBOX_BACK_BUTTON);

		DRIVER_LTRIGGER = new TriggerButton(DriverController, HAND.LEFT);
		DRIVER_RTRIGGER = new TriggerButton(DriverController, HAND.RIGHT);
		
		// Driver Controller -> Command Mapping
		DRIVER_A.whenPressed(new MoveInfeedArmsToPresetPosition(INFEED_ARM_TARGET_POSITION.SQUEEZE));
		DRIVER_B.whenPressed(new MoveInfeedArmsToPresetPosition(INFEED_ARM_TARGET_POSITION.WIDE));
		DRIVER_X.whenPressed(new MoveInfeedArmsToPresetPosition(INFEED_ARM_TARGET_POSITION.STORE));
		DRIVER_Y.whenPressed(new ZeroInfeedArms());
		DRIVER_LBUMPER.whenPressed(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.SpinCW));
		DRIVER_RBUMPER.whenPressed(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.SpinCCW));
		DRIVER_LTRIGGER.whenPressed(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.Infeed));
		DRIVER_RTRIGGER.whenPressed(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.Outfeed)); 
		DRIVER_START_BUTTON.whenPressed(new ShiftGear());
		
		DRIVER_LBUMPER.whenReleased(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.StopWheels));
		DRIVER_RBUMPER.whenReleased(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.StopWheels));
		DRIVER_LTRIGGER.whenReleased(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.StopWheels));
		DRIVER_RTRIGGER.whenReleased(new RunInfeedWheels(INFEED_WHEELS_FUNCTION.StopWheels)); 
				
		// =========== Operator ======================================
		OperatorController = new XboxController(RobotMap.OPERATOR_GAMEPAD_USB_PORT);
		//==========================================================
		OPERATOR_UP = new JoystickButton(OperatorController, XBOX_UP_PORT);
		OPERATOR_DOWN = new JoystickButton(OperatorController, XBOX_DOWN_PORT);
		OPERATOR_LEFT = new JoystickButton(OperatorController, XBOX_LEFT_PORT);
		OPERATOR_RIGHT = new JoystickButton(OperatorController, XBOX_RIGHT_PORT);
		
		OPERATOR_A = new JoystickButton(OperatorController, XBOX_A_PORT);
		OPERATOR_B = new JoystickButton(OperatorController, XBOX_B_PORT);
		OPERATOR_X = new JoystickButton(OperatorController, XBOX_X_PORT);
		OPERATOR_Y = new JoystickButton(OperatorController, XBOX_Y_PORT);
		
		OPERATOR_LBUMPER = new JoystickButton(OperatorController, XBOX_LBUMPER_PORT);
		OPERATOR_RBUMPER = new JoystickButton(OperatorController, XBOX_RBUMPER_PORT);
		
		OPERATOR_XBOX = new JoystickButton(OperatorController, XBOX_XBOX_PORT);
		
		OPERATOR_LSTICK_BUTTON = new JoystickButton(OperatorController, XBOX_LSTICK_BUTTON_PORT);
		OPERATOR_RSTICK_BUTTON = new JoystickButton(OperatorController, XBOX_RSTICK_BUTTON_PORT);
		
		OPERATOR_START_BUTTON = new JoystickButton(OperatorController, XBOX_START_BUTTON);
		OPERATOR_BACK_BUTTON = new JoystickButton(OperatorController, XBOX_BACK_BUTTON);
		
		// Operator Controller -> Command Mapping
		OPERATOR_A.whenPressed(new MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.INFEED_HEIGHT));
		//OPERATOR_B.whenPressed(new MoveElevatorToPresetPosition(INFEED_ARM_TARGET_POSITION.WIDE));
		//OPERATOR_X.whenPressed(new MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.SCALE_HEIGHT));
		OPERATOR_Y.whenPressed(new MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.SCALE_HEIGHT));
		OPERATOR_BACK_BUTTON.whenPressed(new MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.CLIMB_HEIGHT));
		OPERATOR_RBUMPER.whenPressed(new MoveElevatorToPresetPosition(ELEVATOR_TARGET_POSITION.SWITCH_HEIGHT));
		OPERATOR_LBUMPER.whenPressed(new ToggleClimberServoPosition());
		OPERATOR_START_BUTTON.whenPressed(new ToggleActiveCamera());	
	}
	
	public double getDriver_Throttle_JoystickCmd() {
		if(Math.abs(DriverController.getY(Hand.kLeft)) >= XBOX_AXIS_DEADBAND){
			// flip the sign, pushing the joystick up is a # < 0
			return DriverController.getY(Hand.kLeft) * -1.0;
		} 
		else {
			return 0.0;
		}
	}
	
	public double getDriver_Turn_JoystickCmd() {
		if(Math.abs(DriverController.getX(Hand.kRight)) >= XBOX_AXIS_DEADBAND){
			// flip the sign, pushing the joystick up is a # < 0
			return DriverController.getX(Hand.kRight); //* -1.0;
		} 
		else {
			return 0.0;
		}
	}
	
	public double getOperator_Climber_JoystickCmd() {
		if(Math.abs(OperatorController.getY(Hand.kRight)) >= 0.5){
			// flip the sign, pushing the joystick up is a # < 0
			return OperatorController.getY(Hand.kRight) * -1.0;
		} 
		else {
			return 0.0;
		}
	}
}
