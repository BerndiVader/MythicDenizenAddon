package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;

import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class GetTeam extends AbstractCommand {
	private static String str_name="name";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(str_name) && arg.matchesPrefix(str_name)) {
				entry.addObject(str_name, arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_name)) {
			DenizenAPI.getCurrentInstance().debugMessage("Teamname is required! for getteam command");
		}
		
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String teamName = entry.getElement(str_name).asString();
		if (ScoreBoardsAddon.scoreBoardHasTeam(teamName)) {
			dTeam team = ScoreBoardsAddon.getTeam(teamName);
			entry.addObject("team", team);
		}
	}
}
