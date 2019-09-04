package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

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
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;

public 
class 
MythicMobSkillCast 
extends 
AbstractCommand 
{
	private boolean bool;
	private static BukkitAPIHelper mmapi=MythicMobs.inst().getAPIHelper();
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_caster) && arg.matchesPrefix(Statics.str_caster)) {
				if (arg.matchesArgumentType(EntityTag.class)) {
					entry.addObject(Statics.str_caster,arg.asType(EntityTag.class));
				} else if (arg.matchesArgumentType(PlayerTag.class)) {
					entry.addObject(Statics.str_caster,arg.asType(PlayerTag.class).getDenizenEntity());
				} else if (arg.matchesArgumentType(dActiveMob.class)) {
					entry.addObject(Statics.str_caster,new EntityTag(arg.asType(dActiveMob.class).getEntity()));
				}
			} else if (!entry.hasObject(Statics.str_skill) && arg.matchesPrefix(Statics.str_skill)) {
				entry.addObject(Statics.str_skill,arg.asElement());
			} else if (!entry.hasObject(Statics.str_target) && arg.matchesPrefix(Statics.str_target)) {
				if (arg.getValue().toLowerCase().startsWith("l@")) {
					bool = false;
					entry.addObject(Statics.str_target, arg.asType(LocationTag.class));
				} else {
					bool = true;
					entry.addObject(Statics.str_target, arg.asType(EntityTag.class));
				}
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)) {
				entry.addObject(Statics.str_trigger, arg.asType(EntityTag.class));
			} else if (!entry.hasObject(Statics.str_power) && arg.matchesPrefix(Statics.str_power)
					&& arg.matchesPrimitive(PrimitiveType.Float)) {
				entry.addObject(Statics.str_power, arg.asElement());
			} else if (!entry.hasObject(Statics.str_repeat) && arg.matchesPrefix(Statics.str_repeat) 
					&& arg.matchesPrimitive(PrimitiveType.Integer)) {
				entry.addObject(Statics.str_repeat, arg.asElement());
			} else if (!entry.hasObject(Statics.str_delay) && arg.matchesPrefix(Statics.str_delay) 
					&& arg.matchesPrimitive(PrimitiveType.Integer)) {
				entry.addObject(Statics.str_delay, arg.asElement());
			}
		}
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, entry.getObjectTag(Statics.str_caster));
		}
		if (!entry.hasObject(Statics.str_power)) {
			entry.addObject(Statics.str_power, new ElementTag("1"));
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
		HashSet<Entity> etargets=new HashSet<Entity>();
		HashSet<Location> ltargets=new HashSet<Location>();
		if (bool) {
			Entity e=null;
			if ((e=((EntityTag)entry.getObjectTag(Statics.str_target)).getBukkitEntity())!=null) etargets.add(e);
		} else {
			ltargets.add(((LocationTag)entry.getObjectTag(Statics.str_target)));
		}
		Entity caster=((EntityTag)entry.getObjectTag(Statics.str_caster)).getBukkitEntity();
		Entity trigger = ((EntityTag)entry.getObjectTag(Statics.str_trigger)).getBukkitEntity();
		String skill = entry.getElement(Statics.str_skill).asString();
		float power = entry.getElement(Statics.str_power).asFloat();
		int ttimer = entry.getElement(Statics.str_repeat).asInt();
		long tdelay = entry.getElement(Statics.str_delay).asLong();
    	new BukkitRunnable() {
    		int timer = ttimer;
    		public void run() {
    			if (timer==-1||caster==null) {
    				this.cancel();
    			} else {
    				mmapi.castSkill(caster,skill,trigger,caster.getLocation(),etargets,ltargets,power);
    				timer--;
    			}
            } 
        }.runTaskTimer(MythicDenizenPlugin.inst(), 0, tdelay);
	}
}
