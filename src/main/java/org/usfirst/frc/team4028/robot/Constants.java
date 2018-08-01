package org.usfirst.frc.team4028.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Constants 
{
	// NavX (on Roborio)
	public static final double MAX_PITCH_POSITIVE = 25.0;
	public static final double MAX_PITCH_NEGATIVE = -25.0;
	
	// Solenoid Positions
	public static final Value SHIFTER_LOW_GEAR_POS = DoubleSolenoid.Value.kReverse;
	public static final Value SHIFTER_HIGH_GEAR_POS = DoubleSolenoid.Value.kForward;

	public static final Value CARRIAGE_SQUEEZE_POS = DoubleSolenoid.Value.kForward;
	public static final Value CARRIAGE_WIDE_POS = DoubleSolenoid.Value.kReverse;
	
	public static final Value CARRIAGE_FLAP_UP = DoubleSolenoid.Value.kForward;
	public static final Value CARRIAGE_FLAP_DOWN = DoubleSolenoid.Value.kReverse;
	
	// Logging
	// this is where the USB stick is mounted on the RoboRIO filesystem.  
	// You can confirm by logging into the RoboRIO using WinSCP
	public static final String PRIMARY_LOG_FILE_PATH = "/media/sda1/logging";
	public static final String ALTERNATE_LOG_FILE_PATH = "/media/sdb1/logging";
	
    public static final int BIG_NUMBER = (int)1e6;
    
	/* Robot Physical Constants */
	// Wheels
	public static final double DRIVE_WHEEL_DIAMETER_IN = 6.258;
	public static final double TRACK_WIDTH_INCHES = 24.25;
}
