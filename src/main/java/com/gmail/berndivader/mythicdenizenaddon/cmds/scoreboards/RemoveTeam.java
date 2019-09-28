package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import org.bukkit.scoreboard.Team;

public class RemoveTeam extends AbstractCommand {

    private static String teamName = "name";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(teamName) && arg.matchesPrefix(teamName)) {
                entry.addObject(teamName, arg.asElement());
            }
        }
        if (!entry.hasObject(teamName)) {
            MythicDenizenPlugin.getInstance().getLogger().warning("Teamname is required!");
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        Team team = ScoreBoardsAddon.scoreboard.getTeam(entry.getElement(teamName).asString());
        if (team != null) {
            ScoreBoardsAddon.scoreboard.getTeams().remove(team);
        }
    }
}
