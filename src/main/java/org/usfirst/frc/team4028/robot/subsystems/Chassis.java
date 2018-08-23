package org.usfirst.frc.team4028.robot.subsystems;

//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.Constants;
import org.usfirst.frc.team4028.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4028.robot.sensors.NavXGyro;
import org.usfirst.frc.team4028.robot.util.GeneralUtilities;
import org.usfirst.frc.team4028.robot.util.LogDataBE;
//#endregion

/**
 * This class defines the Chassis Subsystem, it is responsible for:
 * 	- Left & Right drive Motors
 *  - Solenoid that controls the shifting
 */
public class Chassis extends Subsystem 
{

	private TalonSRX _leftMaster, _leftSlave, _rightMaster, _rightSlave;
	private DoubleSolenoid _shifter;
	
	private NavXGyro _navX = NavXGyro.getInstance();
	
	private static final double ENCODER_COUNTS_PER_WHEEL_REV = 30725.425;		// account for gear boxes
	
	//=====================================================================================
	// Define Singleton Pattern
	//=====================================================================================
	private static Chassis _instance = new Chassis();
	
	public static Chassis getInstance() 
	{
		return _instance;
	}
	
	// private constructor for singleton pattern
	private Chassis() {
		_leftMaster = new TalonSRX(RobotMap.LEFT_DRIVE_MASTER_CAN_ADDR);
		_leftSlave = new TalonSRX(RobotMap.LEFT_DRIVE_SLAVE_CAN_ADDR);
		_rightMaster = new TalonSRX(RobotMap.RIGHT_DRIVE_MASTER_CAN_ADDR);
		_rightSlave = new TalonSRX(RobotMap.RIGHT_DRIVE_SLAVE_CAN_ADDR);
		
		_leftSlave.follow(_leftMaster);
		_rightSlave.follow(_rightMaster);
		
		_leftMaster.setInverted(true);
		_leftSlave.setInverted(true);
		_rightMaster.setInverted(false);
		_rightSlave.setInverted(false);
		
		configMasterMotors(_leftMaster);
		configMasterMotors(_rightMaster);
        
        configDriveMotors(_leftMaster);
        configDriveMotors(_rightMaster);
        configDriveMotors(_leftSlave);
        configDriveMotors(_rightSlave);

		_shifter = new DoubleSolenoid(RobotMap.PCM_CAN_ADDR, RobotMap.SHIFTER_EXTEND_PCM_PORT, RobotMap.SHIFTER_RETRACT_PCM_PORT);
	}
	
	/* ===== Chassis State: PERCENT VBUS ===== */
	/** Arcade drive with throttle and turn inputs. Includes anti-tipping. */
	public synchronized void arcadeDrive(double throttle, double turn) {
		//_chassisState = ChassisState.PERCENT_VBUS;
		
		if(_navX.isPitchPastThreshhold()) 
		{
			setLeftRightCommand(ControlMode.PercentOutput, 0.0, 0.0);
			DriverStation.reportError("Tipping Threshold", false);
		} 
		else if ((Math.abs(get_leftVelocityInchesPerSec() - get_rightVelocityInchesPerSec())) < 5.0) {
			setLeftRightCommand(ControlMode.PercentOutput, throttle + 0.7 * turn, throttle - 0.7 * turn);
		} 
		else {
			setLeftRightCommand(ControlMode.PercentOutput, throttle + 0.5 * turn, throttle - 0.5 * turn);
		} 
	}
	
	/* ===== SHIFTER ===== */
    public synchronized void toggleShifter() {
    	setHighGear(!get_isHighGear());	// Inverse of current solenoid state
    }
	
	public synchronized void setHighGear(boolean isHighGear) {
		if (isHighGear) 
			_shifter.set(Constants.SHIFTER_HIGH_GEAR_POS);
		else 
			_shifter.set(Constants.SHIFTER_LOW_GEAR_POS);
	}
	
	private void configMasterMotors(TalonSRX talon) {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 0);
	
        talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms, 0);
        talon.configVelocityMeasurementWindow(32, 0);
        
        talon.configOpenloopRamp(0.4, 10);
        talon.configClosedloopRamp(0.0, 0);
	}
	
	private void configDriveMotors(TalonSRX talon) {
		talon.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
		talon.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled, 0);
        
        talon.enableCurrentLimit(false);
        
        talon.configPeakOutputForward(1.0, 10);
        talon.configPeakOutputReverse(-1.0, 10);
        talon.configNominalOutputForward(0, 10);
        talon.configNominalOutputReverse(0, 10);
        talon.configContinuousCurrentLimit(Constants.BIG_NUMBER, 10);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {}
	
	//=====================================================================================
	// Property Accessors
	//=====================================================================================
	public double get_leftSpeed() {
		return _leftMaster.getSelectedSensorVelocity(0) * (600 / ENCODER_COUNTS_PER_WHEEL_REV);
	}
	
	public double get_rightSpeed() {
		return -_rightMaster.getSelectedSensorVelocity(0) * (600 / ENCODER_COUNTS_PER_WHEEL_REV);
	}

    public double get_leftVelocityInchesPerSec() {
        return rpmToInchesPerSecond(get_leftSpeed());
    }

    public double get_rightVelocityInchesPerSec() {
        return rpmToInchesPerSecond(get_rightSpeed());
	}
	
	private synchronized boolean get_isHighGear() {
		return _shifter.get() == Constants.SHIFTER_HIGH_GEAR_POS;
	}
	
	//=====================================================================================
	// Private Helper methods below
	//=====================================================================================
	private void setLeftRightCommand(ControlMode mode, double leftCommand, double rightCommand) {
		_leftMaster.set(mode, leftCommand);
		_rightMaster.set(mode, rightCommand);
	}
	   
    private static double rpmToInchesPerSecond(double rpm) {
        return rotationsToInches(rpm) / 60;
    }
    
    private static double rotationsToInches(double rot) {
        return rot * (Constants.DRIVE_WHEEL_DIAMETER_IN * Math.PI);
    } 

	//=====================================================================================
	// Support Methods
	//=====================================================================================
	public void updateLogData(LogDataBE logData) 
	{
		logData.AddData("Left Actual Velocity [in/s]", String.valueOf(GeneralUtilities.roundDouble(get_leftVelocityInchesPerSec(), 2)));
		//logData.AddData("Left Target Velocity [in/s]", String.valueOf(GeneralUtilities.roundDouble(_leftTargetVelocity, 2)));
		logData.AddData("Left Output Current", String.valueOf(GeneralUtilities.roundDouble(_leftMaster.getOutputCurrent(), 2)));
		
		logData.AddData("Right Actual Velocity [in/s]", String.valueOf(GeneralUtilities.roundDouble(-get_rightVelocityInchesPerSec(), 2)));
		//logData.AddData("Right Target Velocity [in/s]", String.valueOf(GeneralUtilities.roundDouble(_rightTargetVelocity, 2)));
		logData.AddData("Right Output Current", String.valueOf(GeneralUtilities.roundDouble(_rightMaster.getOutputCurrent(), 2)));
		
		//logData.AddData("Pose X", String.valueOf(RobotState.getInstance().getLatestFieldToVehicle().getValue().getTranslation().x()));
		//logData.AddData("Pose Y", String.valueOf(RobotState.getInstance().getLatestFieldToVehicle().getValue().getTranslation().y()));
		//logData.AddData("Pose Angle", String.valueOf(RobotState.getInstance().getLatestFieldToVehicle().getValue().getRotation().getDegrees()));
		//logData.AddData("Remaining Distance", String.valueOf(getRemainingPathDistance()));
		
		//logData.AddData("Center Target Velocity", String.valueOf(GeneralUtilities.roundDouble(_centerTargetVelocity, 2)));

	}
	
	public void updateDashboard() 
	{
		SmartDashboard.putBoolean("IsHighGear", get_isHighGear());
		SmartDashboard.putNumber("Chassis: Left Velocity", -1); //GeneralUtilities.roundDouble(getLeftVelocityInchesPerSec(), 2));
		SmartDashboard.putNumber("Chassis: Right Velocity", -1); //GeneralUtilities.roundDouble(getLeftVelocityInchesPerSec(), 2));
		
		SmartDashboard.putNumber("Chassis: Left Wheel Target Velocity", -1); //GeneralUtilities.roundDouble(_leftTargetVelocity, 2));
		SmartDashboard.putNumber("Chasiss: Right Wheel Target Velocity", -1); //GeneralUtilities.roundDouble(_leftTargetVelocity, 2));
		
		SmartDashboard.putNumber("Chassis: Angle", -1); //GeneralUtilities.roundDouble(getHeading(), 2));
		SmartDashboard.putString("Chassis: Robot Pose", "N/A"); //RobotState.getInstance().getLatestFieldToVehicle().getValue().toString());

	}
}