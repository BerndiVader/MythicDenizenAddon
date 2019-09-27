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
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.ActivePlayer;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Optional;

public class PlayerSkillCast extends AbstractCommand {

    private static boolean castSkillFromPlayer(Entity e, String skillName, Entity trigger,
                                               Location origin, HashSet<AbstractEntity> feTargets, HashSet<AbstractLocation> flTargets, float power,
                                               int ttimer, long tdelay) {

        Optional<Skill> maybeSkill = MythicMobs.inst().getSkillManager().getSkill(skillName);
        if (!maybeSkill.isPresent()) {
            return false;
        }
        ActivePlayer ap = new ActivePlayer(e);
        Skill skill = maybeSkill.get();
        SkillMetadata data;
        if (skill.usable(data = new SkillMetadata(SkillTrigger.API, ap, BukkitAdapter.adapt(trigger), BukkitAdapter.adapt(origin), feTargets, flTargets, power), SkillTrigger.API)) {
            new BukkitRunnable() {
                int timer = ttimer;

                public void run() {
                    if (timer != -1) {
                        skill.execute(data);
                        timer--;
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(MythicDenizenPlugin.getInstance(), 0, tdelay);
        }
        return true;
    }

    private boolean isEntity;

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.CASTER) && arg.matchesPrefix(StaticStrings.CASTER)
                    && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.CASTER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.SKILL) && arg.matchesPrefix(StaticStrings.SKILL)) {
                entry.addObject(StaticStrings.SKILL, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.TARGET) && arg.matchesPrefix(StaticStrings.TARGET)) {
                isEntity = !arg.getValue().toLowerCase().startsWith("l@");
                entry.addObject(StaticStrings.TARGET, isEntity ? arg.asType(EntityTag.class) : arg.asType(LocationTag.class));
            } else if (!entry.hasObject(StaticStrings.TRIGGER) && arg.matchesPrefix(StaticStrings.TRIGGER)) {
                entry.addObject(StaticStrings.TRIGGER, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.REPEAT) && arg.matchesPrefix(StaticStrings.REPEAT)
                    && arg.matchesPrimitive(PrimitiveType.Integer)) {
                entry.addObject(StaticStrings.REPEAT, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.DELAY) && arg.matchesPrefix(StaticStrings.DELAY)
                    && arg.matchesPrimitive(PrimitiveType.Integer)) {
                entry.addObject(StaticStrings.DELAY, arg.asElement());
            }
        }

        entry.defaultObject(StaticStrings.TRIGGER, entry.getObjectTag(StaticStrings.CASTER));
        entry.defaultObject(StaticStrings.REPEAT, new ElementTag(0));
        entry.defaultObject(StaticStrings.DELAY, new ElementTag(0));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        Entity caster = ((EntityTag) entry.getObjectTag(StaticStrings.CASTER)).getBukkitEntity();
        Entity trigger = ((EntityTag) entry.getObjectTag(StaticStrings.TRIGGER)).getBukkitEntity();
        int ttimer = entry.getElement(StaticStrings.REPEAT).asInt();
        long tdelay = entry.getElement(StaticStrings.DELAY).asLong();
        Entity etarget = null;
        Location ltarget = null;
        if (isEntity) {
            etarget = ((EntityTag) entry.getObjectTag(StaticStrings.TARGET)).getBukkitEntity();
        } else {
            ltarget = ((LocationTag) entry.getObjectTag(StaticStrings.TARGET));
        }
        String skill = entry.getElement(StaticStrings.SKILL).asString();
        HashSet<AbstractEntity> eTargets = new HashSet<>();
        HashSet<AbstractLocation> lTargets = new HashSet<>();
        if (etarget != null) {
            eTargets.add(BukkitAdapter.adapt(etarget));
        }
        if (ltarget != null) {
            lTargets.add(BukkitAdapter.adapt(ltarget));
        }

        castSkillFromPlayer(caster, skill, trigger, caster.getLocation(), eTargets, lTargets, 1.0f, ttimer, tdelay);
    }
}
