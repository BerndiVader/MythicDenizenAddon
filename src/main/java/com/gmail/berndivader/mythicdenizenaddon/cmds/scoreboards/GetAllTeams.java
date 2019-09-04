package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

public class GetAllTeams extends AbstractCommand {
	private static String str_board="board";
	private static String str_main="main";
	private static String str_custom="custom";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(str_board) && arg.matchesPrefix(str_board)) {
				entry.addObject(str_board, new ElementTag(arg.getValue().toLowerCase().equals(str_main)?str_main:str_custom));
			} else arg.reportUnhandled();
		}
		if (!entry.hasObject(str_board)) {
			entry.addObject(str_board, new ElementTag(str_custom));
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String board = entry.getElement(str_board).asString();
		Scoreboard sb = board.toLowerCase().equals(str_main)?Bukkit.getScoreboardManager().getMainScoreboard():ScoreBoardsAddon.scoreboard;
		entry.addObject("teamlist", ScoreBoardsAddon.getAllTeamsOf(sb));
	}
}
