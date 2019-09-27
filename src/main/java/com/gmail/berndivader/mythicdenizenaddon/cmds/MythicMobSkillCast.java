package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class MythicMobSkillCast extends AbstractCommand {

    private boolean isEntityTarget;

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.CASTER) && arg.matchesPrefix(StaticStrings.CASTER)) {
                if (arg.matchesArgumentType(EntityTag.class)) {
                    entry.addObject(StaticStrings.CASTER, arg.asType(EntityTag.class));
                } else if (arg.matchesArgumentType(PlayerTag.class)) {
                    entry.addObject(StaticStrings.CASTER, arg.asType(PlayerTag.class).getDenizenEntity());
                } else if (arg.matchesArgumentType(dActiveMob.class)) {
                    entry.addObject(StaticStrings.CASTER, new EntityTag(arg.asType(dActiveMob.class).getEntity()));
                }
            } else if (!entry.hasObject(StaticStrings.SKILL) && arg.matchesPrefix(StaticStrings.SKILL)) {
                entry.addObject(StaticStrings.SKILL, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.TARGET) && arg.matchesPrefix(StaticStrings.TARGET)) {
                if (arg.getValue().toLowerCase().startsWith("l@")) {
                    isEntityTarget = false;
                    entry.addObject(StaticStrings.TARGET, arg.asType(LocationTag.class));
                } else {
                    isEntityTarget = true;
                    entry.addObject(StaticStrings.TARGET, arg.asType(EntityTag.class));
                }
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.POWER) && arg.matchesPrefix(StaticStrings.POWER)
                    && arg.matchesPrimitive(PrimitiveType.Float)) {
                entry.addObject(StaticStrings.POWER, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.REPEAT) && arg.matchesPrefix(StaticStrings.REPEAT)
                    && arg.matchesPrimitive(PrimitiveType.Integer)) {
                entry.addObject(StaticStrings.REPEAT, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.DELAY) && arg.matchesPrefix(StaticStrings.DELAY)
                    && arg.matchesPrimitive(PrimitiveType.Integer)) {
                entry.addObject(StaticStrings.DELAY, arg.asElement());
            }
        }

        entry.defaultObject(StaticStrings.TRIGGER, entry.getObjectTag(StaticStrings.CASTER));
        entry.defaultObject(StaticStrings.POWER, new ElementTag(1));
        entry.defaultObject(StaticStrings.REPEAT, new ElementTag(0));
        entry.defaultObject(StaticStrings.DELAY, new ElementTag(0));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        HashSet<Entity> eTargets = new HashSet<>();
        HashSet<Location> lTargets = new HashSet<>();
        if (isEntityTarget) {
            Entity e = ((EntityTag) entry.getObjectTag(StaticStrings.TARGET)).getBukkitEntity();
            if (e != null) {
                eTargets.add(e);
            }
        } else {
            lTargets.add(((LocationTag) entry.getObjectTag(StaticStrings.TARGET)));
        }
        Entity caster = ((EntityTag) entry.getObjectTag(StaticStrings.CASTER)).getBukkitEntity();
        Entity trigger = ((EntityTag) entry.getObjectTag(StaticStrings.TRIGGER)).getBukkitEntity();
        String skill = entry.getElement(StaticStrings.SKILL).asString();
        float power = entry.getElement(StaticStrings.POWER).asFloat();
        int triggerTimer = entry.getElement(StaticStrings.REPEAT).asInt();
        long triggerDelay = entry.getElement(StaticStrings.DELAY).asLong();
        new BukkitRunnable() {
            int timer = triggerTimer;

            public void run() {
                if (timer == -1 || caster == null) {
                    this.cancel();
                } else {
                    MythicMobsAddon.mythicApiHelper.castSkill(caster, skill, trigger, caster.getLocation(), eTargets, lTargets, power);
                    timer--;
                }
            }
        }.runTaskTimer(MythicDenizenPlugin.getInstance(), 0, triggerDelay);
    }
}
