package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;
import org.apache.commons.lang3.tuple.Pair;

public class TriggerSkill extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.ACTIVE_MOB) && arg.matchesPrefix(StaticStrings.ACTIVE_MOB)
                    && arg.matchesArgumentType(dActiveMob.class)) {
                entry.addObject(arg.getPrefix().getValue(), arg.asType(dActiveMob.class));
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)) {
                entry.addObject(arg.getPrefix().getValue(), arg.asElement());
            } else if (!entry.hasObject(StaticStrings.ENTITY) && arg.matchesPrefix(StaticStrings.ENTITY)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(arg.getPrefix().getValue(), arg.asType(EntityTag.class));
            }
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        if (!entry.hasObject(StaticStrings.ACTIVE_MOB) || !entry.hasObject(StaticStrings.ENTITY) || !entry.hasObject(StaticStrings.TRIGGER)) {
            return;
        }

        SkillTrigger trigger;
        try {
            trigger = SkillTrigger.valueOf(entry.getElement(StaticStrings.TRIGGER).asString().toUpperCase());
        } catch (Exception ex) {
            Debug.echoError(ex.getMessage());
            return;
        }

        ActiveMob am = ((dActiveMob) entry.getObject(StaticStrings.ACTIVE_MOB)).getActiveMob();
        AbstractEntity ae = BukkitAdapter.adapt(((EntityTag) entry.getObject(StaticStrings.ENTITY)).getBukkitEntity());
        new TriggeredSkill(trigger, am, ae, new Pair[0]);
    }
}
