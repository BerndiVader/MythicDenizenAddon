package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.Types;
import com.gmail.berndivader.mythicdenizenaddon.obj.ActivePlayer;

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

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.caster.a()) && arg.matchesPrefix(Types.caster.a()) 
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.caster.a(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.skill.a()) && arg.matchesPrefix(Types.skill.a())) {
				entry.addObject(Types.skill.a(), arg.asElement());
			} else if (!entry.hasObject(Types.target.a()) && arg.matchesPrefix(Types.target.a())) {
				bool=!arg.getValue().toLowerCase().startsWith("l@");
				entry.addObject(Types.target.a(),bool?arg.asType(dEntity.class):arg.asType(dLocation.class));
			} else if (!entry.hasObject(Types.trigger.a()) && arg.matchesPrefix(Types.trigger.a())) {
				entry.addObject(Types.trigger.a(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.repeat.a()) && arg.matchesPrefix(Types.repeat.a()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.repeat.a(), arg.asElement());
			} else if (!entry.hasObject(Types.delay.a()) && arg.matchesPrefix(Types.delay.a()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.delay.a(), arg.asElement());
			}
		}
		if (!entry.hasObject(Types.trigger.a())) {
			entry.addObject(Types.trigger.a(), (dEntity)entry.getdObject(Types.caster.a()));
		}
		if (!entry.hasObject(Types.repeat.a())) {
			entry.addObject(Types.repeat.a(), new Element("0"));
		}
		if (!entry.hasObject(Types.delay.a())) {
			entry.addObject(Types.delay.a(), new Element("0"));
		}
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Entity caster = ((dEntity)entry.getdObject(Types.caster.a())).getBukkitEntity();
		Entity trigger = ((dEntity)entry.getdObject(Types.trigger.a())).getBukkitEntity();
		int ttimer = entry.getElement(Types.repeat.a()).asInt();
		long tdelay = entry.getElement(Types.delay.a()).asLong();
		Entity etarget = null;
		Location ltarget = null;
		if (bool) {
			etarget = ((dEntity)entry.getdObject(Types.target.a())).getBukkitEntity();
		} else {
			ltarget = ((dLocation)entry.getdObject(Types.target.a()));
		}
		String skill = entry.getElement(Types.skill.a()).asString();
        HashSet<AbstractEntity> eTargets = new HashSet<AbstractEntity>();
        HashSet<AbstractLocation> lTargets = new HashSet<AbstractLocation>();
        if (etarget != null) eTargets.add(BukkitAdapter.adapt(etarget));
        if (ltarget != null) lTargets.add(BukkitAdapter.adapt(ltarget));
		castSkillFromPlayer(caster, skill, trigger, caster.getLocation(), eTargets, lTargets, 1.0f, ttimer, tdelay);
	}
	
	private static boolean castSkillFromPlayer(Entity e, String skillName, Entity trigger, 
			Location origin, HashSet<AbstractEntity> feTargets, HashSet<AbstractLocation> flTargets, float power,
			int ttimer, long tdelay) {

        Optional<Skill>maybeSkill=MythicMobs.inst().getSkillManager().getSkill(skillName);
        if (!maybeSkill.isPresent()) return false;
        ActivePlayer ap=new ActivePlayer(e);
        Skill skill=maybeSkill.get();
		SkillMetadata data;
        if (skill.usable(data=new SkillMetadata(SkillTrigger.API, ap,BukkitAdapter.adapt(trigger),BukkitAdapter.adapt(origin),feTargets,flTargets,power),SkillTrigger.API)) {
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
