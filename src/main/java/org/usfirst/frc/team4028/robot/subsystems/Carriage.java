package org.usfirst.frc.team4028.robot.subsystems;

import org.usfirst.frc.team4028.robot.Constants;
import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.util.LogDataBE;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class defines the Carriage Subsystem, it is responsible for:
 * 	- Left & Right Motors that drive the infeed/outfeed wheels
 *  - Solenoid that controls the squeeze
 *  - Solenoid that controls the tilt
 *  - Limit Switch that indicates a cube is fully in the carriage
 */
public class Carriage extends Subsystem 
{
	// define class level working variables
	private TalonSRX _carriageLeftMotor; 
	private TalonSRX _carriageRightMotor;
	
	private DigitalInput _carriageLimitSwitch;
	private DoubleSolenoid _squeezeCylinder;
	private DoubleSolenoid _tiltCylinder;
	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static Carriage _instance = new Carriage();
	
	public static Carriage getInstance() {
		return _instance;
	}
	
	// private constructor for singleton pattern
	private Carriage() {
		//====================================================================================
		//	config master & slave talon objects
		//====================================================================================
		_carriageLeftMotor = new TalonSRX(RobotMap.CARRIAGE_LEFT_CAN_ADDRESS);
		_carriageRightMotor = new TalonSRX(RobotMap.CARRIAGE_RIGHT_CAN_ADDRESS);
		
		// set motor phasing
		_carriageLeftMotor.setInverted(false);
		_carriageRightMotor.setInverted(true);
		
		// config limit switches
		_carriageLeftMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		_carriageLeftMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		_carriageRightMotor.configForwardLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, 0);
		_carriageRightMotor.configReverseLimitSwitchSource(RemoteLimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0, 0);
		
		// turn off all soft limits
		_carriageLeftMotor.configForwardSoftLimitEnable(false, 0);
		_carriageLeftMotor.configReverseSoftLimitEnable(false, 0);
		_carriageRightMotor.configForwardSoftLimitEnable(false, 0);
		_carriageRightMotor.configReverseSoftLimitEnable(false, 0);
		
		// config brake mode
		_carriageLeftMotor.setNeutralMode(NeutralMode.Coast);
		_carriageRightMotor.setNeutralMode(NeutralMode.Coast);
		
		//Enable Current Limiting
		_carriageLeftMotor.enableCurrentLimit(true);
		_carriageLeftMotor.configPeakCurrentDuration(200, 0);
		_carriageLeftMotor.configPeakCurrentLimit(17, 0);
		
		// config quad encoder & phase (invert = true)
		_carriageLeftMotor.configSelectedFeedbackSensor(FeedbackDevice.None, 0, 0);
		
		//configure the peak and nominal output voltages in both directions for both Talons
		_carriageLeftMotor.configNominalOutputForward(0, 0);
		_carriageLeftMotor.configNominalOutputReverse(0, 0);
		_carriageLeftMotor.configPeakOutputForward(1, 0);
		_carriageLeftMotor.configPeakOutputReverse(-1, 0);
		
		_carriageRightMotor.configNominalOutputForward(0, 0);
		_carriageRightMotor.configNominalOutputReverse(0, 0);
		_carriageRightMotor.configPeakOutputForward(1, 0);
		_carriageRightMotor.configPeakOutputReverse(-1, 0);
		
		// set motor mode
		_carriageLeftMotor.set(ControlMode.PercentOutput, 0, 0);
		_carriageRightMotor.set(ControlMode.PercentOutput, 0, 0);
	
		// DisableSoftLimits
		_carriageLeftMotor.configReverseSoftLimitEnable(false, 0);
		_carriageLeftMotor.configForwardSoftLimitEnable(false, 0);
		_carriageRightMotor.configReverseSoftLimitEnable(false, 0);
		_carriageRightMotor.configForwardSoftLimitEnable(false, 0);
		
		//Setup Limit Switch
		_carriageLimitSwitch = new DigitalInput(RobotMap.CARRIAGE_LIMIT_SWITCH_DIO_PORT);
		
		//Setup Solenoid for Cylinder
		_squeezeCylinder = new DoubleSolenoid(RobotMap.PCM_CAN_ADDR, RobotMap.CARRIAGE_SQUEEZE_PCM_PORT, RobotMap.CARRIAGE_WIDE_PCM_PORT);
		_squeezeCylinder.set(Constants.CARRIAGE_WIDE_POS);
		
		//Setup Solenoid for Tilt
		_tiltCylinder = new DoubleSolenoid(RobotMap.PCM_CAN_ADDR, RobotMap.CARRIAGE_FLAP_UP_PCM_PORT, RobotMap.CARRIAGE_FLAP_DOWN_PCM_PORT);
		
		//this.tiltCarriageDown();
		
		//_carriageWheelsState = CARRIAGE_WHEELS_STATE.STOPPED;
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void updateLogData(LogDataBE logData) 
	{
	}
	
	public void outputToShuffleboard() 
	{
	}
}