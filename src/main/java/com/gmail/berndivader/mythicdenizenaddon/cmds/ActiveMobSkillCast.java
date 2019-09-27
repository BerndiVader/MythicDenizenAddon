package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.HashSet;

public class ActiveMobSkillCast extends AbstractCommand {

    private boolean isTargetEntity;

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.CASTER) && arg.matchesPrefix(StaticStrings.CASTER)
                    && arg.matchesArgumentType(dActiveMob.class)) {
                entry.addObject(StaticStrings.CASTER, arg.asType(dActiveMob.class));
            } else if (!entry.hasObject(StaticStrings.SKILL) && arg.matchesPrefix(StaticStrings.SKILL)) {
                entry.addObject(StaticStrings.SKILL, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.TARGET) && arg.matchesPrefix(StaticStrings.TARGET)) {
                if (arg.getValue().toLowerCase().startsWith("l@")) {
                    isTargetEntity = false;
                    entry.addObject(StaticStrings.TARGET, arg.asType(LocationTag.class));
                } else {
                    isTargetEntity = true;
                    entry.addObject(StaticStrings.TARGET, arg.asType(EntityTag.class));
                }
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.POWER) && arg.matchesPrefix(StaticStrings.POWER)
                    && arg.matchesPrimitive(PrimitiveType.Float)) {
                entry.addObject(StaticStrings.POWER, arg.asElement());
            }
        }

        entry.defaultObject(StaticStrings.TRIGGER, new EntityTag(((dActiveMob) entry.getObjectTag(StaticStrings.CASTER)).getEntity()));
        entry.defaultObject(StaticStrings.POWER, new ElementTag(1));
    }


    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        HashSet<Entity> eTargets = new HashSet<>();
        HashSet<Location> lTargets = new HashSet<>();

        if (isTargetEntity) {
            eTargets.add(((EntityTag) entry.getObjectTag(StaticStrings.TARGET)).getBukkitEntity());
        } else {
            lTargets.add((entry.getObjectTag(StaticStrings.TARGET)));
        }

        ActiveMob caster = ((dActiveMob) entry.getObjectTag(StaticStrings.CASTER)).getActiveMob();
        Entity trigger = ((EntityTag) entry.getObjectTag(StaticStrings.TRIGGER)).getBukkitEntity();
        String skill = entry.getElement(StaticStrings.SKILL).asString();
        float power = entry.getElement(StaticStrings.POWER).asFloat();

        MythicMobsAddon.mythicApiHelper.castSkill(BukkitAdapter.adapt(caster.getEntity()), skill, trigger, BukkitAdapter.adapt(caster.getLocation()), eTargets, lTargets, power);
    }
}
