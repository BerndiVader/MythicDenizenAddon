package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import org.bukkit.entity.Entity;

public class GetEntityByEntry extends AbstractCommand {

    private static String entityEntry = "entry";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(entityEntry) && arg.matchesPrefix(entityEntry)) {
                entry.addObject(entityEntry, arg.asElement());
            }
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        String e = entry.getElement(entityEntry).asString();
        Entity entity = ScoreBoardsAddon.getEntityByEntry(e);
        if (entity != null) {
            entry.addObject(entityEntry, new EntityTag(entity));
        } else {
            entry.addObject(entityEntry, new ElementTag(false));
        }
    }
}
