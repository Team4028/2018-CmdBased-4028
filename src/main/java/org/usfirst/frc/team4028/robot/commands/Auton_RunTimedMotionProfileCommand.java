package org.usfirst.frc.team4028.robot.commands;

import org.usfirst.frc.team4028.robot.auton.pathfollowing.RobotState;
import org.usfirst.frc.team4028.robot.auton.pathfollowing.control.Path;
import org.usfirst.frc.team4028.robot.subsystems.Chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Auton_RunTimedMotionProfileCommand extends Command
{
    Chassis _chassis = Chassis.getInstance();
    private Path _path;
    private double _startTime;
    double _maxTime;

    public Auton_RunTimedMotionProfileCommand(Path p, double maxTime)
    {
        requires(_chassis);
        _maxTime = maxTime;
        _path = p;
    }

    @Override
    protected void initialize() {
        RobotState.getInstance().reset(Timer.getFPGATimestamp(), _path.getStartPose());
		_chassis.setWantDrivePath(_path, _path.isReversed());
		//_chassis.setHighGear(true);
		_startTime = Timer.getFPGATimestamp();
    }
    @Override
    protected void execute() {
        if(Timer.getFPGATimestamp() - _startTime > 0.25) {
			if(_chassis.get_leftPos() == 0 || _chassis.get_rightPos() == 0) {
				_chassis.forceDoneWithPath();
				System.out.println("Attention Idiots: You Morons Forgot to Plug in The Encoder");
			}
		}
    }
    @Override
    protected boolean isFinished() {
        //System.out.println("Does the bloody Motion Profile Comand know how freaking lucky it is to Finish?");
        if (Math.floor(Timer.getFPGATimestamp() * 1000) % 1000 == 0){
            System.out.println("Second gotten to:" + Timer.getFPGATimestamp());
        } if (_chassis.isDoneWithPath() || Timer.getFPGATimestamp()-_startTime>=_maxTime){
            System.out.println("Motion Profile Terminating");
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void end() {
        System.out.println("Motion Profile Properly Terminated");
        _chassis.stop();
    }

}