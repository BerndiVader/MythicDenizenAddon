package com.gmail.berndivader.mmDenizenAddon.plugins.cmds.scoreboards;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;
import com.gmail.berndivader.mmDenizenAddon.plugins.obj.scoreboards.dTeam;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class getTeam extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("name") && arg.matchesPrefix("name")) {
				entry.addObject("name", arg.asElement());
			} else arg.reportUnhandled();
		}
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String teamName = entry.getElement("name").asString();
		if (ScoreBoardsAddon.scoreBoardHasTeam(teamName)) {
			dTeam team = ScoreBoardsAddon.getTeam(teamName);
			entry.addObject("team", team);
		}
	}
}
