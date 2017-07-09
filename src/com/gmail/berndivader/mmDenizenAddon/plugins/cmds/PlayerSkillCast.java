package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmDenizenAddon.MythicDenizenPlugin;
import com.gmail.berndivader.mmDenizenAddon.plugins.obj.ActivePlayer;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class PlayerSkillCast extends AbstractCommand {
	private boolean bool;
	private enum Types {
		caster,
		skill,
		target,
		trigger,
		repeat,
		delay
	}

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.caster.name()) && arg.matchesPrefix(Types.caster.name()) 
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.caster.name(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.skill.name()) && arg.matchesPrefix(Types.skill.name())) {
				entry.addObject(Types.skill.name(), arg.asElement());
			} else if (!entry.hasObject(Types.target.name()) && arg.matchesPrefix(Types.target.name())) {
				if (arg.getValue().toLowerCase().startsWith("l@")) {
					bool = false;
					entry.addObject(Types.target.name(), arg.asType(dLocation.class));
				} else {
					bool = true;
					entry.addObject(Types.target.name(), arg.asType(dEntity.class));
				}
			} else if (!entry.hasObject(Types.trigger.name()) && arg.matchesPrefix(Types.trigger.name())) {
				entry.addObject(Types.trigger.name(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.repeat.name()) && arg.matchesPrefix(Types.repeat.name()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.repeat.name(), arg.asElement());
			} else if (!entry.hasObject(Types.delay.name()) && arg.matchesPrefix(Types.delay.name()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.delay.name(), arg.asElement());
			}
		}
		if (!entry.hasObject(Types.trigger.name())) {
			entry.addObject(Types.trigger.name(), (dEntity)entry.getdObject(Types.caster.name()));
		}
		if (!entry.hasObject(Types.repeat.name())) {
			entry.addObject(Types.repeat.name(), new Element("0"));
		}
		if (!entry.hasObject(Types.delay.name())) {
			entry.addObject(Types.delay.name(), new Element("0"));
		}
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Entity caster = ((dEntity)entry.getdObject(Types.caster.name())).getBukkitEntity();
		Entity trigger = ((dEntity)entry.getdObject(Types.trigger.name())).getBukkitEntity();
		int ttimer = entry.getElement(Types.repeat.name()).asInt();
		long tdelay = entry.getElement(Types.delay.name()).asLong();
		Entity etarget = null;
		Location ltarget = null;
		if (bool) {
			etarget = ((dEntity)entry.getdObject(Types.target.name())).getBukkitEntity();
		} else {
			ltarget = ((dLocation)entry.getdObject(Types.target.name()));
		}
		String skill = entry.getElement(Types.skill.name()).asString();
        HashSet<AbstractEntity> eTargets = new HashSet<AbstractEntity>();
        HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
        if (etarget != null) eTargets.add(BukkitAdapter.adapt(etarget));
        if (ltarget != null) lTargets.add(BukkitAdapter.adapt(ltarget));
		castSkillFromPlayer(caster, skill, trigger, caster.getLocation(), eTargets, lTargets, 1.0f, ttimer, tdelay);
	}
	
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
        			if (timer!=-1) {
        				skill.execute(data);
        				timer--;
        			} else {
        				this.cancel();
        			}
                } 
            }.runTaskTimer(MythicDenizenPlugin.inst(), 0, tdelay);
        }
		return true;
	}
	
}
