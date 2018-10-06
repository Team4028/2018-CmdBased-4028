/*package org.usfirst.frc.team4028.robot.commands;

import org.omg.CORBA.PRIVATE_MEMBER;
//#region  == Define Imports ==
import org.usfirst.frc.team4028.robot.subsystems.Climber;
import org.usfirst.frc.team4028.robot.subsystems.Elevator;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
//#endregion
import edu.wpi.first.wpilibj.command.CommandGroup;


  This command implements support for toggling the position of the Climber Servo
 

public class Simultaneous_Command extends CommandGroup
{
    public Simultaneous_Command(List<Command> commands) {
		for (int i = 0; i< commands.size()-1; i++ ) {
            addParallel(commands.get(i));
		 }
		 addParallel(commands.get(commands.size()-1));
		}

}

*/

package org.usfirst.frc.team4028.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import java.util.ArrayList;
import java.util.List;

/* Allows for multiple actions to run in parallel */
public class Simultaneous_Command extends Command{
	private final ArrayList<Command> _commandList;
	
	public Simultaneous_Command(List<Command> commandList) {
		_commandList = new ArrayList<>(commandList.size());
		for (Command command : commandList) {
			_commandList.add(command);
		}
	}
	
	@Override
	public void initialize() {
		for (Command command : _commandList) {
			command.start();	// Start all actions
		}
	}

	@Override
	public void execute() {
	}

	@Override
	public void end() {	
		for (Command command : _commandList) {
			command.cancel();	// Call when ALL actions are finished
		}
	}

	@Override
	public boolean isFinished() {	// Returns true when ALL actions are finished
		boolean isAllFinished = true;
		for (Command command : _commandList) {
			if (!command.isCompleted()) {
				isAllFinished = false;	
			}
		}
		
		if (isAllFinished){
			System.out.println("Simultaneous Command Terminating");
			return true;
		} else {
			return false;
		}
	}
}