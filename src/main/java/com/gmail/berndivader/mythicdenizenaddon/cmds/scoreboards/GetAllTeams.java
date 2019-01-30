package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class GetAllTeams extends AbstractCommand {
	private static String str_board="board";
	private static String str_main="main";
	private static String str_custom="custom";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(str_board) && arg.matchesPrefix(str_board)) {
				entry.addObject(str_board, new Element(arg.getValue().toLowerCase().equals(str_main)?str_main:str_custom));
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_board)) {
			entry.addObject(str_board, new Element(str_custom));
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String board = entry.getElement(str_board).asString();
		Scoreboard sb = board.toLowerCase().equals(str_main)?Bukkit.getScoreboardManager().getMainScoreboard():ScoreBoardsAddon.scoreboard;
		entry.addObject("teamlist", ScoreBoardsAddon.getAllTeamsOf(sb));
	}
}
