package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.entity.Entity;

public class SendSignal extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.ACTIVE_MOB) && arg.matchesPrefix(StaticStrings.ACTIVE_MOB)
                    && arg.matchesArgumentType(dActiveMob.class)) {
                entry.addObject(StaticStrings.ACTIVE_MOB, arg.asType(dActiveMob.class));
            } else if (!entry.hasObject(StaticStrings.SIGNAL) && arg.matchesPrefix(StaticStrings.SIGNAL)) {
                entry.addObject(StaticStrings.SIGNAL, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            }
        }

        entry.defaultObject(StaticStrings.TRIGGER, new EntityTag(((dActiveMob) entry.getObject(StaticStrings.ACTIVE_MOB)).getEntity()));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        ActiveMob am = ((dActiveMob) entry.getObject(StaticStrings.ACTIVE_MOB)).getActiveMob();
        String signal = entry.getElement(StaticStrings.SIGNAL).asString();
        Entity trigger = ((EntityTag) entry.getObject(StaticStrings.TRIGGER)).getBukkitEntity();

        am.signalMob(BukkitAdapter.adapt(trigger), signal);
    }
}
