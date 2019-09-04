package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

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
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.ActivePlayer;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

public 
class 
PlayerSkillCast
extends 
AbstractCommand
{
	private boolean bool;

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_caster) && arg.matchesPrefix(Statics.str_caster) 
					&& arg.matchesArgumentType(EntityTag.class)) {
				entry.addObject(Statics.str_caster, arg.asType(EntityTag.class));
			} else if (!entry.hasObject(Statics.str_skill) && arg.matchesPrefix(Statics.str_skill)) {
				entry.addObject(Statics.str_skill, arg.asElement());
			} else if (!entry.hasObject(Statics.str_target) && arg.matchesPrefix(Statics.str_target)) {
				bool=!arg.getValue().toLowerCase().startsWith("l@");
				entry.addObject(Statics.str_target,bool?arg.asType(EntityTag.class):arg.asType(LocationTag.class));
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)) {
				entry.addObject(Statics.str_trigger, arg.asType(EntityTag.class));
			} else if (!entry.hasObject(Statics.str_repeat) && arg.matchesPrefix(Statics.str_repeat) 
					&& arg.matchesPrimitive(PrimitiveType.Integer)) {
				entry.addObject(Statics.str_repeat, arg.asElement());
			} else if (!entry.hasObject(Statics.str_delay) && arg.matchesPrefix(Statics.str_delay) 
					&& arg.matchesPrimitive(PrimitiveType.Integer)) {
				entry.addObject(Statics.str_delay, arg.asElement());
			}
		}
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, (EntityTag)entry.getObjectTag(Statics.str_caster));
		}
		if (!entry.hasObject(Statics.str_repeat)) {
			entry.addObject(Statics.str_repeat, new ElementTag("0"));
		}
		if (!entry.hasObject(Statics.str_delay)) {
			entry.addObject(Statics.str_delay, new ElementTag("0"));
		}
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Entity caster = ((EntityTag)entry.getObjectTag(Statics.str_caster)).getBukkitEntity();
		Entity trigger = ((EntityTag)entry.getObjectTag(Statics.str_trigger)).getBukkitEntity();
		int ttimer = entry.getElement(Statics.str_repeat).asInt();
		long tdelay = entry.getElement(Statics.str_delay).asLong();
		Entity etarget = null;
		Location ltarget = null;
		if (bool) {
			etarget = ((EntityTag)entry.getObjectTag(Statics.str_target)).getBukkitEntity();
		} else {
			ltarget = ((LocationTag)entry.getObjectTag(Statics.str_target));
		}
		String skill = entry.getElement(Statics.str_skill).asString();
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
