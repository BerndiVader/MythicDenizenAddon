package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

import java.util.AbstractMap;
import java.util.HashSet;

public class CreateMythicMeta extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.CAUSE) && arg.matchesPrefix(StaticStrings.CAUSE)) {
                entry.addObject(StaticStrings.CAUSE, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.CASTER) && arg.matchesPrefix(StaticStrings.CASTER)) {
                if (arg.matchesArgumentType(EntityTag.class)) {
                    entry.addObject(StaticStrings.CASTER, arg.asType(EntityTag.class));
                }
                else if (arg.matchesArgumentType(dActiveMob.class)) {
                    entry.addObject(StaticStrings.CASTER, arg.asType(dActiveMob.class));
                }
                else {
                    entry.addObject(StaticStrings.CASTER, null);
                }
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.ORIGIN) && arg.matchesPrefix(StaticStrings.ORIGIN)
                    && arg.matchesArgumentType(LocationTag.class)) {
                entry.addObject(StaticStrings.ORIGIN, arg.asType(LocationTag.class));
            } else if (!entry.hasObject(StaticStrings.TARGETS) && arg.matchesPrefix(StaticStrings.TARGETS)
                    && arg.matchesArgumentType(ListTag.class)) {
                entry.addObject(StaticStrings.TARGETS, arg.asType(ListTag.class));
            } else if (!entry.hasObject(StaticStrings.POWER) && arg.matchesPrefix(StaticStrings.POWER)
                    && arg.matchesPrimitive(PrimitiveType.Float)) {
                entry.addObject(StaticStrings.POWER, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.RESULT) && arg.matchesPrefix(StaticStrings.RESULT)) {
                entry.addObject(StaticStrings.RESULT, arg.asElement());
            }
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        GenericCaster caster = null;
        AbstractEntity trigger = null;
        SkillTrigger cause = SkillTrigger.API;
        AbstractLocation origin = null;
        HashSet<AbstractEntity> entityTargets = new HashSet<>();
        HashSet<AbstractLocation> locationTargets = new HashSet<>();
        float power = 1f;
        String result = StaticStrings.RESULT;

        if (entry.hasObject(StaticStrings.CAUSE)) {
            SkillTrigger getCause = Utils.enumLookup(SkillTrigger.class, entry.getElement(StaticStrings.CAUSE).asString().toUpperCase());
            if (getCause != null) {
                cause = getCause;
            }
        }
        if (entry.hasObject(StaticStrings.CASTER)) {
            caster = new GenericCaster(BukkitAdapter.adapt(((EntityTag) entry.getObjectTag(StaticStrings.CASTER)).getBukkitEntity()));
        }
        if (entry.hasObject(StaticStrings.TRIGGER)) {
            trigger = BukkitAdapter.adapt(((EntityTag) entry.getObjectTag(StaticStrings.TRIGGER)).getBukkitEntity());
        }
        if (entry.hasObject(StaticStrings.ORIGIN)) {
            origin = BukkitAdapter.adapt(((LocationTag) entry.getObjectTag(StaticStrings.ORIGIN)));
        }
        if (entry.hasObject(StaticStrings.POWER)) {
            power = entry.getElement(StaticStrings.POWER).asFloat();
        }
        if (entry.hasObject(StaticStrings.TARGETS)) {
            AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>> pair = Utils.splitTargetList((ListTag) entry.getObject(StaticStrings.TARGETS));
            entityTargets = pair.getKey();
            locationTargets = pair.getValue();
            if (entityTargets.size() > 0) {
                locationTargets = null;
            }
        }
        if (entry.hasObject(StaticStrings.RESULT)) {
            result = entry.getElement(StaticStrings.RESULT).asString();
        }

        entry.addObject(result, new dMythicMeta(new SkillMetadata(cause, caster, trigger, origin, entityTargets, locationTargets, power)));
    }

}
