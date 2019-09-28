package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicSkill;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

import java.util.AbstractMap;
import java.util.HashSet;

public class ExecuteMythicMobsSkill extends AbstractCommand {

    static String errorStr = "ExecuteMythicMobsSkill - argument %s is required!";

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.SKILL) && arg.matchesPrefix(StaticStrings.SKILL)) {
                if (arg.matchesArgumentType(dMythicSkill.class)) {
                    entry.addObject(StaticStrings.SKILL, arg.asType(dMythicSkill.class));
                } else if (arg.matchesPrimitive(PrimitiveType.String)) {
                    entry.addObject(StaticStrings.SKILL, new dMythicSkill(arg.asElement().asString()));
                }
            } else if (!entry.hasObject(StaticStrings.DATA) && arg.matchesPrefix(StaticStrings.DATA)
                    && arg.matchesArgumentType(dMythicMeta.class)) {
                entry.addObject(StaticStrings.DATA, arg.asType(dMythicMeta.class));
            } else if (!entry.hasObject(StaticStrings.CASTER) && arg.matchesPrefix(StaticStrings.CASTER)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.CASTER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.ORIGIN) && arg.matchesPrefix(StaticStrings.ORIGIN)
                    && arg.matchesArgumentType(LocationTag.class)) {
                entry.addObject(StaticStrings.ORIGIN, arg.asType(LocationTag.class));
            } else if (!entry.hasObject(StaticStrings.CAUSE) && arg.matchesPrefix(StaticStrings.CAUSE)) {
                entry.addObject(StaticStrings.CAUSE, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.POWER) && arg.matchesPrefix(StaticStrings.POWER)
                    && arg.matchesPrimitive(PrimitiveType.Double)) {
                entry.addObject(StaticStrings.POWER, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.TARGETS) && arg.matchesPrefix(StaticStrings.TARGETS)) {
                if (arg.matchesArgumentType(ListTag.class)) {
                    entry.addObject(StaticStrings.TARGETS, arg.asType(ListTag.class));
                } else if (arg.matchesArgumentType(EntityTag.class)) {
                    ListTag list = new ListTag();
                    list.addObject(arg.asType(EntityTag.class));
                    entry.addObject(StaticStrings.TARGETS, list);
                }
            }
        }

        if (!entry.hasObject(StaticStrings.SKILL)) {
            Debug.echoError(entry.getResidingQueue(), String.format(errorStr, StaticStrings.SKILL));
        }

    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        SkillMetadata meta = null;
        dMythicSkill mythicSkill = entry.getObjectTag(StaticStrings.SKILL);

        if (entry.hasObject(StaticStrings.DATA)) {
            meta = ((dMythicMeta) entry.getObjectTag(StaticStrings.DATA)).getSkillMetadata();
        } else {
            GenericCaster caster = null;
            AbstractEntity trigger = null;
            AbstractLocation origin = null;
            SkillTrigger cause = SkillTrigger.API;
            float power = 1f;
            HashSet<AbstractLocation> locations = new HashSet<>();
            HashSet<AbstractEntity> entities = new HashSet<>();

            if (mythicSkill.hasMeta()) {
                meta = mythicSkill.getSkillMetadata();
            }

            EntityTag entityCaster = entry.getObjectTag(StaticStrings.CASTER);
            EntityTag entityTrigger = entry.getObjectTag(StaticStrings.TRIGGER);
            LocationTag locationOrigin = entry.getObjectTag(StaticStrings.ORIGIN);
            ElementTag elCause = entry.getElement(StaticStrings.CAUSE);
            ElementTag elPower = entry.getElement(StaticStrings.POWER);
            ListTag targetList = entry.getObjectTag(StaticStrings.TARGETS);

            if (entityCaster != null) {
                caster = new GenericCaster(BukkitAdapter.adapt(entityCaster.getBukkitEntity()));
            }
            if (entityTrigger != null) {
                trigger = BukkitAdapter.adapt(entityTrigger.getBukkitEntity());
            }
            if (locationOrigin != null) {
                origin = BukkitAdapter.adapt(locationOrigin);
            }
            if (elCause != null) {
                cause = Utils.enumLookup(SkillTrigger.class, elCause.asString());
            }
            if (elPower != null) {
                power = elPower.asFloat();
            }
            if (targetList != null) {
                AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>> pair = Utils.splitTargetList(targetList);
                locations = pair.getValue();
                entities = pair.getKey();
            }

            if (meta == null) {
                meta = new SkillMetadata(cause, caster, trigger, origin, entities, locations, power);
            } else {
                if (caster != null) {
                    meta.setCaster(caster);
                }
                if (trigger != null) {
                    meta.setTrigger(trigger);
                }
                if (origin != null) {
                    meta.setOrigin(origin);
                }
                if (!locations.isEmpty()) {
                    meta.setLocationTargets(locations);
                } else if (!entities.isEmpty()) {
                    meta.setEntityTargets(entities);
                }
            }
        }
        mythicSkill.execute(meta);
    }
}
