package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMechanic;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

public class GetMythicMechanic extends AbstractCommand {

    static String errorArgParsing = "GetMythicMechanic - argument %s is required!";
    static String errorExecution = "GetMythicMechanic - mechanic with the name %s is not present!";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.NAME) && arg.matchesPrefix(StaticStrings.NAME)) {
                entry.addObject(StaticStrings.NAME, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.LINE) && arg.matchesPrefix(StaticStrings.LINE)) {
                entry.addObject(StaticStrings.LINE, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.DATA) && arg.matchesPrefix(StaticStrings.DATA)) {
                entry.addObject(StaticStrings.DATA, arg.asType(dMythicMeta.class));
            }
        }

        if (!entry.hasObject(StaticStrings.NAME)) {
            Debug.echoError(entry.getResidingQueue(), String.format(errorArgParsing, StaticStrings.NAME));
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        ElementTag elMechanic = entry.getElement(StaticStrings.NAME);
        ElementTag elLine = entry.getElement(StaticStrings.LINE);

        String line = elLine != null ? elLine.asString() : null;
        String mechanicName = elLine != null ? elMechanic.asString() : null;

        if (mechanicName != null) {
            dMythicMechanic mythicMechanic = new dMythicMechanic(mechanicName, line);
            if (!mythicMechanic.isPresent()) {
                Debug.echoError(entry.getResidingQueue(), String.format(errorExecution, mechanicName));
            }
            entry.addObject(StaticStrings.MECHANIC, mythicMechanic);
        } else {
            Debug.echoError(entry.getResidingQueue(), String.format(errorArgParsing, StaticStrings.NAME));
        }
    }
}
