package org.usfirst.frc.team4028.robot.subsystems;

import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.commands.ClimbWithControllers;
import org.usfirst.frc.team4028.robot.util.LogDataBE;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class defines the Climber Subsystem, it is responsible for:
 * 	- Drive Motor
 */
public class Climber extends Subsystem
{
	// define class level working variables
	private TalonSRX _climberMotor; 

	public static final double CLIMBER_MOTOR_HIGH_VBUS = 1.0;
	public static final double CLIMBER_MOTOR_LOW_VBUS = 0.40;
	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static Climber _instance = new Climber();
	
	public static Climber getInstance() {
		return _instance;
	}
	
	// private constructor for singleton pattern
	private Climber() 
	{
		//====================================================================================
		//	config master & slave talon objects
		//====================================================================================
		_climberMotor = new TalonSRX(RobotMap.CLIMBER_CAN_ADDRESS);
		
		// set motor phasing
		_climberMotor.setInverted(false);
		
		// config limit switches
		_climberMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		_climberMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		
		// turn off all soft limits
		_climberMotor.configForwardSoftLimitEnable(false, 0);
		_climberMotor.configReverseSoftLimitEnable(false, 0);
		
		// config brake mode
		_climberMotor.setNeutralMode(NeutralMode.Brake);
	
		//Enable Current Limiting
		_climberMotor.enableCurrentLimit(false);
			
		//configure the peak and nominal output voltages in both directions for both Talons
		_climberMotor.configNominalOutputForward(0, 0);
		_climberMotor.configNominalOutputReverse(0, 0);
		_climberMotor.configPeakOutputForward(1, 0);
		_climberMotor.configPeakOutputReverse(-1, 0);
		
		// set motor mode
		_climberMotor.set(ControlMode.PercentOutput, 0, 0);
	
		// DisableSoftLimits
		_climberMotor.configReverseSoftLimitEnable(false, 0);
		_climberMotor.configForwardSoftLimitEnable(false, 0);
				
	}
	
	public void runMotor(double vbusCmd)
	{
		if(vbusCmd > 0.8) { // more than 1/2 way, climb @ high speed
			_climberMotor.set(ControlMode.PercentOutput, CLIMBER_MOTOR_HIGH_VBUS, 0);
		}
		else if(vbusCmd > 0.05)	{ // more than 1/2 way, climb @ low speed
			_climberMotor.set(ControlMode.PercentOutput, CLIMBER_MOTOR_LOW_VBUS, 0);
		}
		else {
			_climberMotor.set(ControlMode.PercentOutput, 0.0, 0);
		}
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ClimbWithControllers());
    }
	
	//=====================================================================================
	// Property Accessors
	//=====================================================================================
	private double get_climberMotorCurrent()	{
		return _climberMotor.getOutputCurrent();
	}

	private boolean get_isClimberServoOpen()	{
		return false; //_climberMotor.getOutputCurrent();
	}

	//=====================================================================================
	// LogData & Dashboard cmds
	//=====================================================================================
	public void updateLogData(LogDataBE logData) 
	{
		logData.AddData("Climber: Current", String.valueOf(get_climberMotorCurrent()));
	}
	
	public void updateDashboard() 
	{
		SmartDashboard.putNumber("Climber:Current:", get_climberMotorCurrent());
		//SmartDashboard.putBoolean("Is Climber Servo Open?:", _isClimberServoOpen);
	}
}