package com.gmail.berndivader.mmDenizenAddon.plugins.cmds.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class removeTeam extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("name") && arg.matchesPrefix("name")) {
				entry.addObject("name", arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject("name")) Bukkit.getLogger().warning("Teamname is required!");
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Team team = ScoreBoardsAddon.scoreboard.getTeam(entry.getElement("name").asString());
		if (team!=null) ScoreBoardsAddon.scoreboard.getTeams().remove(team);
	}
}
