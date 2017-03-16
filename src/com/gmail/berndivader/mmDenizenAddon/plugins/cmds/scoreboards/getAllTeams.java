package com.gmail.berndivader.mmDenizenAddon.plugins.cmds.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class getAllTeams extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("board") && arg.matchesPrefix("board")) {
				entry.addObject("board", new Element(arg.getValue().toLowerCase().equals("main")?"main":"custom"));
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject("board")) {
			entry.addObject("board", new Element("custom"));
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String board = entry.getElement("board").asString();
		Scoreboard sb = board.toLowerCase().equals("main")?Bukkit.getScoreboardManager().getMainScoreboard():ScoreBoardsAddon.scoreboard;
		entry.addObject("teamlist", ScoreBoardsAddon.getAllTeamsOf(sb));
	}
}
