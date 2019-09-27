package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class GetAllTeams extends AbstractCommand {

    private static String board = "board";
    private static String mainBoardName = "main";
    private static String customBoardName = "custom";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(board) && arg.matchesPrefix(board)) {
                entry.addObject(board, new ElementTag(arg.getValue().toLowerCase().equals(mainBoardName) ? mainBoardName : customBoardName));
            }
        }

        entry.defaultObject(board, new ElementTag(customBoardName));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        String board = entry.getElement(GetAllTeams.board).asString();
        Scoreboard sb = board.toLowerCase().equals(mainBoardName) ? Bukkit.getScoreboardManager().getMainScoreboard() : ScoreBoardsAddon.scoreboard;
        entry.addObject("teamlist", ScoreBoardsAddon.getAllTeamsOf(sb));
    }
}
