package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class RemoveTeam extends AbstractCommand {
	private static String str_name="name";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(str_name) && arg.matchesPrefix(str_name)) {
				entry.addObject(str_name, arg.asElement());
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_name)) {
			MythicDenizenPlugin.inst().getLogger().warning("Teamname is required!");
		}
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Team team = ScoreBoardsAddon.scoreboard.getTeam(entry.getElement(str_name).asString());
		if (team!=null) ScoreBoardsAddon.scoreboard.getTeams().remove(team);
	}
}
