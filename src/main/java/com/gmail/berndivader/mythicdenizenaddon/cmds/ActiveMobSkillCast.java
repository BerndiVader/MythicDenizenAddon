package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class ActiveMobSkillCast extends AbstractCommand {
	private boolean bool;
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Statics.str_caster) && arg.matchesPrefix(Statics.str_caster)
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Statics.str_caster, arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_skill) && arg.matchesPrefix(Statics.str_skill)) {
				entry.addObject(Statics.str_skill, arg.asElement());
			} else if (!entry.hasObject(Statics.str_target) && arg.matchesPrefix(Statics.str_target)) {
				if (arg.getValue().toLowerCase().startsWith("l@")) {
					bool = false;
					entry.addObject(Statics.str_target, arg.asType(dLocation.class));
				} else {
					bool = true;
					entry.addObject(Statics.str_target, arg.asType(dEntity.class));
				}
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)) {
				entry.addObject(Statics.str_trigger, arg.asType(dEntity.class));
			} else if (!entry.hasObject(Statics.str_power) && arg.matchesPrefix(Statics.str_power)
					&& arg.matchesPrimitive(aH.PrimitiveType.Float)) {
				entry.addObject(Statics.str_power, arg.asElement());
			}
		}
		
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, new dEntity(((dActiveMob)entry.getdObject(Statics.str_caster)).getEntity()));
		}
		if (!entry.hasObject(Statics.str_power)) {
			entry.addObject(Statics.str_power, new Element("1"));
		}
	}
	
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		HashSet<Entity> etargets = new HashSet<Entity>();
		HashSet<Location> ltargets = new HashSet<Location>();
		if (bool) {
			etargets.add(((dEntity)entry.getdObject(Statics.str_target)).getBukkitEntity());
		} else {
			ltargets.add(((dLocation)entry.getdObject(Statics.str_target)));
		}
		ActiveMob caster = ((dActiveMob)entry.getdObject(Statics.str_caster)).getActiveMob();
		Entity trigger = ((dEntity)entry.getdObject(Statics.str_trigger)).getBukkitEntity();
		String skill = entry.getElement(Statics.str_skill).asString();
		float power = entry.getElement(Statics.str_power).asFloat();
		MythicMobs.inst().getAPIHelper().castSkill(BukkitAdapter.adapt(caster.getEntity()), skill, trigger, BukkitAdapter.adapt(caster.getLocation()), etargets, ltargets, power);
	}
}
