package org.usfirst.frc.team4028.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

/* Selects, runs and stops auton */
public class AutonExecuter {
	private CommandGroup _autoMode;
	private Thread _autoThread = null;
	
	public void setAutoMode(CommandGroup autoMode) {
		_autoMode = autoMode;
	}
	
	public void start() {
		// Creates a new thread to run auton in
		if (_autoThread == null) {
			_autoThread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (_autoMode != null) {
                        _autoMode.start();
					}
				}
			});
			_autoThread.start();
			
		}
	}
	
	public void stop() {
		if (_autoMode != null) {
			_autoMode = null;
		}
		_autoThread = null;
	}
}