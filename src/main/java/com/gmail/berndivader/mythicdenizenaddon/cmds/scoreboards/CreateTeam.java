package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;

public class CreateTeam extends AbstractCommand {
	private static String str_name="name";
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(str_name) && arg.matchesPrefix(str_name)) {
				entry.addObject(str_name, arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_name)) Debug.echoError("Teamname for createTeam is required.");
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Team team;
		String name =ScoreBoardsAddon.trimmedTeamName(entry.getElement(str_name).asString());
		team = ScoreBoardsAddon.scoreboard.getTeam(name)==null?ScoreBoardsAddon.scoreboard.registerNewTeam(name)
				:ScoreBoardsAddon.scoreboard.getTeam(name);
		entry.addObject("team", new dTeam(team));
	}
}
