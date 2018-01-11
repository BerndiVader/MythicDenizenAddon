package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;

import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class CreateTeam extends AbstractCommand {
	private static String str_name="name";
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(str_name) && arg.matchesPrefix(str_name)) {
				entry.addObject(str_name, arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_name)) DenizenAPI.getCurrentInstance().debugError("No Teamname for createTeam.");
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
