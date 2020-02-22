package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.scoreboard.Team;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

public
class 
RemoveTeam 
extends 
AbstractCommand
{
	private static String str_name="name";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(str_name) && arg.matchesPrefix(str_name)) {
				entry.addObject(str_name, arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_name)) {
			MythicDenizenPlugin.inst().getLogger().warning("Teamname is required!");
		}
	}

	@Override
	public void execute(ScriptEntry entry) {
		Team team = ScoreBoardsAddon.scoreboard.getTeam(entry.getElement(str_name).asString());
		if (team!=null) ScoreBoardsAddon.scoreboard.getTeams().remove(team);
	}
}
