package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class CreateMythicSpawner extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("bla") && arg.matchesPrefix()) {
				
			} else arg.reportUnhandled();
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		// TODO Auto-generated method stub
	}
}
