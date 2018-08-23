package org.usfirst.frc.team4028.robot.subsystems;

//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.RobotMap;
import org.usfirst.frc.team4028.robot.util.LogDataBE;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
//#endregion

/**
 * This class defines the Elevator Subsystem, it is responsible for:
 * 	- Drive Motor
 *  - Fwd & Rev (Home) Limit Swit
 */
public class Elevator extends Subsystem 
{
	
	// =================================================================================================================
	// define class level working variables
	private TalonSRX _elevatorMotor;
	
	private int _targetElevatorPositionNU;
	private int _autonCustomPositionNU = 0;
	
	private int _elevatorPositionOffsetNU;
		
	private double _actualPositionNU = 0;
	private double _actualVelocityNU_100mS = 0;
	private double _actualAccelerationNU_100mS_mS = 0;
	private long _lastScanTimeStamp = 0;
	private double _lastScanActualVelocityNU_100mS = 0;
	private int _pidSlotInUse = -1;
	private boolean _isClimbBumpValueEnabled = false;
	private boolean _hasElevatorBeenZeroed = false;
	
	private int _currentUpAccelerationConstant = TELEOP_UP_ACCELERATION; 
	private int _currentDownAccelerationConstant = TELEOP_DOWN_ACCELERATION;
		
	
	// =================================================================================================================
	// Define Enums for the Elevator Axis
	public enum ELEVATOR_TARGET_POSITION {
		HOME,
		INFEED_HEIGHT,
		SCALE_HEIGHT,
		CLIMB_HEIGHT,
		SWITCH_HEIGHT,
		AUTON_CUSTOM
	}
		
	// =================================================================================================================
	private static final double ELEVATOR_POS_ALLOWABLE_ERROR_IN_INCHES = 0.5;	// +/- 0.25
	private static final int ELEVATOR_POS_ALLOWABLE_ERROR_IN_NU = InchesToNativeUnits(ELEVATOR_POS_ALLOWABLE_ERROR_IN_INCHES);
	
	//Conversion Constant
	public static final double NATIVE_UNITS_PER_INCH_CONVERSION = (28510/78.75);
	
	// hardcoded preset positions (in native units, 0 = home position)
	private static final int SCALE_HEIGHT_POSITION_IN_NU = InchesToNativeUnits(72.5);
	private static final int UNSAFE_SCALE_HEIGHT_POSITION_IN_NU = InchesToNativeUnits(65); //Determines how fast we should drive
	private static final int SWITCH_HEIGHT_POSITION_IN_NU = InchesToNativeUnits(30);
	private static final int INFEED_POSITION_IN_NU = 0;
	private static final int HOME_POSITION_IN_NU = 0;
	private static final int FLAP_DOWN_BELOW_HEIGHT_POSITION_IN_NU = InchesToNativeUnits(54);
	private static final int CLIMB_HEIGHT_POSITION_IN_NU  = InchesToNativeUnits(40.5);
	private static final int CLIMB_CLICK_ON_BAR_HEIGHT_IN_NU = InchesToNativeUnits(63);
	
	
	//Bump Position Up/Down on Elevator Constant
	private static final int LARGE_BUMP_AMOUNT_IN_NU = InchesToNativeUnits(3);
	private static final int SMALL_BUMP_AMOUNT_CLIMB_IN_NU = InchesToNativeUnits(1);
	
	private static final double MAX_BUMP_UP_AMOUNT = InchesToNativeUnits(8.9); 
	private static final double MAX_BUMP_DOWN_AMOUNT = InchesToNativeUnits(-20.9);
		
  	// define PID Constants	
	private static final int MOVING_DOWN_PID_SLOT_INDEX = 0;
	private static final int MOVING_UP_PID_SLOT_INDEX = 1;
	private static final int HOLDING_PID_SLOT_INDEX = 2;

	public static final int TELEOP_UP_ACCELERATION = 4500;	// native units per 100 mSec per sec
	public static final int AUTON_UP_ACCELERATION = 10000; // native units per 100 mSec per sec
	public static final int TELEOP_DOWN_ACCELERATION = 2500; // native units per 100 mSec per sec
	public static final int AUTON_DOWN_ACCELERATION = 6000; // native units per 100 mSec per sec
	
	public static final int UP_CRUISE_VELOCITY = 4000; // native units per 100 mSec 50% of max
	public static final int DOWN_CRUISE_VELOCITY = 4000; // native units per 100 mSec 50% of max
	
	/*		 VALUES FOR DIFFERENT GEAR BOXES:
	|Gear Ratio|Velocity|Acceleration|Feed Forward|
	|   35:1   |  4000  |    4500    |    0.4     |
	|   30:1   |        |            |            |
	|   40:1   |        |            |            |
	*/
	
	public static final double FEED_FORWARD_GAIN_HOLD = 1.0;
	public static final double PROPORTIONAL_GAIN_HOLD  = 0.65;
	public static final double INTEGRAL_GAIN_HOLD  = 0;
	public static final int INTEGRAL_ZONE_HOLD = 0; 
	public static final double DERIVATIVE_GAIN_HOLD  = 40;
	
	public static final double FEED_FORWARD_GAIN_UP = 0.4;
	public static final double PROPORTIONAL_GAIN_UP = 1.5;
	public static final double INTEGRAL_GAIN_UP = 0;
	public static final int INTEGRAL_ZONE_UP = 0; 
	public static final double DERIVATIVE_GAIN_UP = 75;
	
	public static final double FEED_FORWARD_GAIN_DOWN = 0.2;
	public static final double PROPORTIONAL_GAIN_DOWN = 0.1;
	public static final double INTEGRAL_GAIN_DOWN = 0;
	public static final int INTEGRAL_ZONE_DOWN = 0; 
	public static final double DERIVATIVE_GAIN_DOWN = 2;
	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static Elevator _instance = new Elevator();
	
	public static Elevator getInstance() 
	{
		return _instance;
	}

	// private constructor
	private Elevator() {
		// config master & slave talon objects
		_elevatorMotor = new TalonSRX(RobotMap.ELEVATOR_LIFT_MASTER_CAN_ADDRESS);

		// set motor phasing
		_elevatorMotor.setInverted(false);
		
		// config limit switches
		_elevatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 0);
		_elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed, 0);

		// turn off all soft limits
		_elevatorMotor.configForwardSoftLimitEnable(false, 0);
		_elevatorMotor.configReverseSoftLimitEnable(false, 0);

		// config brake mode
		_elevatorMotor.setNeutralMode(NeutralMode.Brake);
		
		// config quad encoder & phase (invert = true)
		_elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		_elevatorMotor.setSensorPhase(true);
		_elevatorMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 0);
		
		//configure the peak and nominal output voltages in both directions for both Talons
		_elevatorMotor.configNominalOutputForward(0, 0);	// elimate rattle w/i deadband
		_elevatorMotor.configNominalOutputReverse(0, 0);
		_elevatorMotor.configPeakOutputForward(1, 0);
		_elevatorMotor.configPeakOutputReverse(-1, 0);
		
		// config velocity measurement (2x of scan time, looper is 10 mS)
		_elevatorMotor.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_5Ms, 0);
		_elevatorMotor.configVelocityMeasurementWindow(32, 0);
		
		// Setup MotionMagic Mode
		SetPidSlotToUse("constr", MOVING_DOWN_PID_SLOT_INDEX);
		
		// set closed loop gains		
		_elevatorMotor.config_kF(MOVING_DOWN_PID_SLOT_INDEX, FEED_FORWARD_GAIN_DOWN, 0);
		_elevatorMotor.config_kP(MOVING_DOWN_PID_SLOT_INDEX, PROPORTIONAL_GAIN_DOWN, 0);
		_elevatorMotor.config_kI(MOVING_DOWN_PID_SLOT_INDEX, INTEGRAL_GAIN_DOWN, 0);
		_elevatorMotor.config_kD(MOVING_DOWN_PID_SLOT_INDEX, DERIVATIVE_GAIN_DOWN, 0);
		_elevatorMotor.config_IntegralZone(MOVING_DOWN_PID_SLOT_INDEX, INTEGRAL_ZONE_DOWN, 0);
		
		_elevatorMotor.config_kF(MOVING_UP_PID_SLOT_INDEX, FEED_FORWARD_GAIN_UP, 0);
		_elevatorMotor.config_kP(MOVING_UP_PID_SLOT_INDEX, PROPORTIONAL_GAIN_UP, 0);
		_elevatorMotor.config_kI(MOVING_UP_PID_SLOT_INDEX, INTEGRAL_GAIN_UP, 0);
		_elevatorMotor.config_kD(MOVING_UP_PID_SLOT_INDEX, DERIVATIVE_GAIN_UP, 0);
		_elevatorMotor.config_IntegralZone(MOVING_UP_PID_SLOT_INDEX, INTEGRAL_ZONE_UP, 0);
		
		_elevatorMotor.config_kF(HOLDING_PID_SLOT_INDEX, FEED_FORWARD_GAIN_HOLD, 0);
		_elevatorMotor.config_kP(HOLDING_PID_SLOT_INDEX, PROPORTIONAL_GAIN_HOLD, 0);
		_elevatorMotor.config_kI(HOLDING_PID_SLOT_INDEX, INTEGRAL_GAIN_HOLD, 0);
		_elevatorMotor.config_kD(HOLDING_PID_SLOT_INDEX, DERIVATIVE_GAIN_HOLD, 0);
		_elevatorMotor.config_IntegralZone(HOLDING_PID_SLOT_INDEX, INTEGRAL_ZONE_HOLD, 0);
		
		// set accel and cruise velocities
		_elevatorMotor.configMotionCruiseVelocity(UP_CRUISE_VELOCITY, 0);
		_elevatorMotor.configMotionAcceleration(TELEOP_UP_ACCELERATION, 0);
		
		// set allowable closed loop gain
		_elevatorMotor.configAllowableClosedloopError(0, ELEVATOR_POS_ALLOWABLE_ERROR_IN_NU, 0);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
        
	public boolean isElevatorAtUnsafeHeight() {
		return getActualPositionNU() > UNSAFE_SCALE_HEIGHT_POSITION_IN_NU;
	}
	
	public boolean isFlapUpEnabledHeight() {
		return getActualPositionNU() >= FLAP_DOWN_BELOW_HEIGHT_POSITION_IN_NU;
	}
	
	public boolean isClimberServoEnabledHeight() {
		return getActualPositionNU() >= CLIMB_HEIGHT_POSITION_IN_NU;
	}
	
	private int getActualPositionNU()
	{
		return _elevatorMotor.getSelectedSensorPosition(0);	// tomb ADD AFTER 1ST MATCH
	}
	
	public void updateLogData(LogDataBE logData) {
	}
	
	public void updateDashboard() {
	}
	
	// =================================================================================================================
	// Public methods to move the elevator
	// =================================================================================================================
	// Support Operators Gamepad Buttons mapped to discrete positions
	// Note: gets spammed by CubeHandler!
	
	public void initReZeroElevator() {
		_hasElevatorBeenZeroed = false;
	}
	
	public void zeroElevator() {		
		// ==== left side ====
		if (_elevatorMotor.getSensorCollection().isRevLimitSwitchClosed() == false) {
			_hasElevatorBeenZeroed = true;
		}
		else if (_hasElevatorBeenZeroed == false) {
		}
	}
		
	public void MoveToPresetPosition(ELEVATOR_TARGET_POSITION presetPosition) {
		// ignore move requests while in homing process
		if(getHasElevatorBeenZeroed()) {
			switch(presetPosition) {
				case HOME:
					_targetElevatorPositionNU = HOME_POSITION_IN_NU;
					break;
					
				case INFEED_HEIGHT:
					_targetElevatorPositionNU = INFEED_POSITION_IN_NU;
					break;

				case SWITCH_HEIGHT:
					_targetElevatorPositionNU = SWITCH_HEIGHT_POSITION_IN_NU + _elevatorPositionOffsetNU;
					break;
										
				case SCALE_HEIGHT:
					_targetElevatorPositionNU = SCALE_HEIGHT_POSITION_IN_NU + _elevatorPositionOffsetNU;
					break;
				
				case CLIMB_HEIGHT:
					_targetElevatorPositionNU = CLIMB_HEIGHT_POSITION_IN_NU + _elevatorPositionOffsetNU;
					break;
					
				case AUTON_CUSTOM:
					_targetElevatorPositionNU = _autonCustomPositionNU;
					break;
			}
		}
		
		
		// set appropriate gain slot to use (only flip if outside deadband)
		int currentError = Math.abs(_elevatorMotor.getSelectedSensorPosition(0) - _targetElevatorPositionNU);
        if (currentError > ELEVATOR_POS_ALLOWABLE_ERROR_IN_NU) {
			if(_targetElevatorPositionNU > _actualPositionNU) {
				SetPidSlotToUse("MoveUp", MOVING_UP_PID_SLOT_INDEX);
			} else {
				SetPidSlotToUse("MoveDown", MOVING_DOWN_PID_SLOT_INDEX);
			}
		}
		_elevatorMotor.set(ControlMode.MotionMagic, _targetElevatorPositionNU);
		System.out.println(_elevatorPositionOffsetNU);
	}
		
	private void SetPidSlotToUse(String ref, int pidSlot) {
		if(pidSlot != _pidSlotInUse) {
			//ReportStateChg("Chg Pid Slot: Ref: [" + ref + "] [" + _pidSlotInUse + "] => [" + pidSlot + "]");
			_pidSlotInUse = pidSlot;
			_elevatorMotor.selectProfileSlot(_pidSlotInUse, 0);
			
			if(pidSlot == MOVING_UP_PID_SLOT_INDEX) {
				_elevatorMotor.configMotionCruiseVelocity(UP_CRUISE_VELOCITY, 0);
				_elevatorMotor.configMotionAcceleration(_currentUpAccelerationConstant, 0);
			}
			else if(pidSlot == MOVING_DOWN_PID_SLOT_INDEX) {
				_elevatorMotor.configMotionCruiseVelocity(DOWN_CRUISE_VELOCITY, 0);
				_elevatorMotor.configMotionAcceleration(_currentDownAccelerationConstant, 0);
			}
		}
	}

	public void elevatorPositionBumpUp() {
		if(_elevatorPositionOffsetNU < MAX_BUMP_UP_AMOUNT) {
			if(_isClimbBumpValueEnabled) {
				if (getElevatorActualPositionNU() < 20000) {
					_elevatorPositionOffsetNU = (CLIMB_CLICK_ON_BAR_HEIGHT_IN_NU - CLIMB_HEIGHT_POSITION_IN_NU);
				}
			} else {
				_elevatorPositionOffsetNU = _elevatorPositionOffsetNU + LARGE_BUMP_AMOUNT_IN_NU;
			}
		} else {
			System.out.println("Elevator Scale Position Bump Tooooooo Large");
		}		
	}
	
	public void elevatorPositionBumpDown() {
		if(_elevatorPositionOffsetNU > MAX_BUMP_DOWN_AMOUNT) {
			if(_isClimbBumpValueEnabled) {
				_elevatorPositionOffsetNU = _elevatorPositionOffsetNU - SMALL_BUMP_AMOUNT_CLIMB_IN_NU;
			} else {
				_elevatorPositionOffsetNU = _elevatorPositionOffsetNU - LARGE_BUMP_AMOUNT_IN_NU;
			}
		} else {
			System.out.println("Elevator Scale Position Bump Tooooooo Large");
		}
	}

	public void resetElevatorBumpValue() {
		_elevatorPositionOffsetNU = 0;
	}

	// ===============================================================================================================
	// Expose Properties of Elevator
	// ===============================================================================================================
	private boolean IsAtTargetPosition(int targetPosition) {
		int currentError = Math.abs(_elevatorMotor.getSelectedSensorPosition(0) - targetPosition);
        if (currentError <= ELEVATOR_POS_ALLOWABLE_ERROR_IN_NU) {
            return true;
        } else {
	      	return false;
	    }
    }
	
	public boolean IsAtTargetPosition() {
		return IsAtTargetPosition(_targetElevatorPositionNU);
	}

	public boolean getHasElevatorBeenZeroed() {
		return _hasElevatorBeenZeroed;
	}

	public double getElevatorActualPositionNU() {
		return _elevatorMotor.getSelectedSensorPosition(0);
	}

	public double getElevatorActualPositionIn() {
		return NativeUnitsToInches(_elevatorMotor.getSelectedSensorPosition(0));
	}

	// ===============================================================================================================
	// General Purpose Utility Methods
	// ===============================================================================================================
	private static int InchesToNativeUnits(double positionInInches) {
		int nativeUnits = (int)(positionInInches * NATIVE_UNITS_PER_INCH_CONVERSION);
		return nativeUnits;
	}
	
	private static double NativeUnitsToInches(double nativeUnitsMeasure) {
		double positionInInches = nativeUnitsMeasure / NATIVE_UNITS_PER_INCH_CONVERSION;
		return positionInInches;
	}
	
}