package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;

public class GetTeam extends AbstractCommand {

    private static String teamName = "name";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(teamName) && arg.matchesPrefix(teamName)) {
                entry.addObject(teamName, arg.asElement());
            }
        }

        if (!entry.hasObject(teamName)) {
            Debug.echoError("Teamname is required! for getteam command");
        }

    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        String teamName = entry.getElement(GetTeam.teamName).asString();
        dTeam team = new dTeam(null);
        if (ScoreBoardsAddon.scoreBoardHasTeam(teamName)) {
            team = ScoreBoardsAddon.getTeam(teamName);
        }
        entry.addObject("team", team);
    }
}
