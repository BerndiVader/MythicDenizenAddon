package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dActiveMob;

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
	private enum Types {
		caster,
		skill,
		target,
		trigger,
		power
	}
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.caster.name()) && arg.matchesPrefix(Types.caster.name()) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.caster.name(), arg.asType(dActiveMob.class));
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
			} else if (!entry.hasObject(Types.power.name()) && arg.matchesPrefix(Types.power.name())
					&& arg.matchesPrimitive(aH.PrimitiveType.Float)) {
				entry.addObject(Types.power.name(), arg.asElement());
			}
		}
		
		if (!entry.hasObject(Types.trigger.name())) {
			entry.addObject(Types.trigger.name(), new dEntity(((dActiveMob)entry.getdObject(Types.caster.name())).getEntity()));
		}
		if (!entry.hasObject(Types.power.name())) {
			entry.addObject(Types.power.name(), new Element("1"));
		}
	}
	
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		HashSet<Entity> etargets = new HashSet<Entity>();
		HashSet<Location> ltargets = new HashSet<Location>();
		if (bool) {
			etargets.add(((dEntity)entry.getdObject(Types.target.name())).getBukkitEntity());
		} else {
			ltargets.add(((dLocation)entry.getdObject(Types.target.name())));
		}
		ActiveMob caster = ((dActiveMob)entry.getdObject(Types.caster.name())).getActiveMob();
		Entity trigger = ((dEntity)entry.getdObject(Types.trigger.name())).getBukkitEntity();
		String skill = entry.getElement(Types.skill.name()).asString();
		float power = entry.getElement(Types.power.name()).asFloat();
		MythicMobs.inst().getAPIHelper().castSkill(BukkitAdapter.adapt(caster.getEntity()), skill, trigger, BukkitAdapter.adapt(caster.getLocation()), etargets, ltargets, power);
	}
}
