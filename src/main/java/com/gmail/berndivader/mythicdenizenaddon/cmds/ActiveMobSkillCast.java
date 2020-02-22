package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public 
class 
ActiveMobSkillCast 
extends
AbstractCommand 
{
	private boolean bool;
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_caster) && arg.matchesPrefix(Statics.str_caster)
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Statics.str_caster, arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_skill) && arg.matchesPrefix(Statics.str_skill)) {
				entry.addObject(Statics.str_skill, arg.asElement());
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
			}
		}
		
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, new EntityTag(((dActiveMob)entry.getObjectTag(Statics.str_caster)).getEntity()));
		}
		if (!entry.hasObject(Statics.str_power)) {
			entry.addObject(Statics.str_power, new ElementTag("1"));
		}
	}
	
	
	@Override
	public void execute(ScriptEntry entry) {
		HashSet<Entity> etargets = new HashSet<Entity>();
		HashSet<Location> ltargets = new HashSet<Location>();
		if (bool) {
			etargets.add(((EntityTag)entry.getObjectTag(Statics.str_target)).getBukkitEntity());
		} else {
			ltargets.add(((LocationTag)entry.getObjectTag(Statics.str_target)));
		}
		ActiveMob caster = ((dActiveMob)entry.getObjectTag(Statics.str_caster)).getActiveMob();
		Entity trigger = ((EntityTag)entry.getObjectTag(Statics.str_trigger)).getBukkitEntity();
		String skill = entry.getElement(Statics.str_skill).asString();
		float power = entry.getElement(Statics.str_power).asFloat();
		MythicMobs.inst().getAPIHelper().castSkill(BukkitAdapter.adapt(caster.getEntity()), skill, trigger, BukkitAdapter.adapt(caster.getLocation()), etargets, ltargets, power);
	}
}
