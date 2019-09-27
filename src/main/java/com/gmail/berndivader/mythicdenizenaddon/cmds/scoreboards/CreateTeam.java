package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;
import org.bukkit.scoreboard.Team;

public class CreateTeam extends AbstractCommand {

    private static String teamName = "name";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(teamName) && arg.matchesPrefix(teamName)) {
                entry.addObject(teamName, arg.asElement());
            }
        }

        if (!entry.hasObject(teamName)) {
            Debug.echoError("Teamname for createTeam is required.");
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        Team team;
        String name = ScoreBoardsAddon.trimmedTeamName(entry.getElement(teamName).asString());
        team = ScoreBoardsAddon.scoreboard.getTeam(name) == null ? ScoreBoardsAddon.scoreboard.registerNewTeam(name)
                : ScoreBoardsAddon.scoreboard.getTeam(name);
        entry.addObject("team", new dTeam(team));
    }
}
