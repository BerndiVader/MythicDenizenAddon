package com.gmail.berndivader.mythicdenizenaddon.cmds.quests;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.events.CustomObjectiveEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.CustomObjectiveEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FireCustomObjectiveEvent extends AbstractCommand {

    static String action = "action";
    static String player = "quester";
    static String objectiveType = "type";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {

        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(action) && arg.matchesPrefix(action)) {
                entry.addObject(action, arg.asElement());
            } else if (!entry.hasObject(player) && arg.matchesPrefix(player) && arg.matchesArgumentType(PlayerTag.class)) {
                entry.addObject(player, arg.asType(PlayerTag.class));
            } else if (!entry.hasObject(objectiveType) && arg.matchesPrefix(objectiveType)) {
                entry.addObject(objectiveType, arg.asElement());
            }
        }

        entry.defaultObject(player, Utilities.getEntryPlayer(entry));
        entry.defaultObject(action, new ElementTag(Action.COMPLETE.toString()));
        entry.defaultObject(objectiveType, new ElementTag(""));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        if (entry.getObjectTag(player) == null) {
            Debug.echoError("A player is required!");
            return;
        }

        Action action = Utils.enumLookup(Action.class, entry.getElement(FireCustomObjectiveEvent.action).asString().toUpperCase());
        Player player = ((PlayerTag) entry.getObjectTag(FireCustomObjectiveEvent.player)).getPlayerEntity();
        String objective_name = entry.getElement(objectiveType).asString();

        CustomObjectiveEvent event = new CustomObjectiveEvent(player, action, objective_name);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

}
