package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public 
class
CreateMythicSpawner
extends 
AbstractCommand 
{

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject("bla") && arg.matchesPrefix()) {
				
			} else arg.reportUnhandled();
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		// TODO Auto-generated method stub
	}
}
