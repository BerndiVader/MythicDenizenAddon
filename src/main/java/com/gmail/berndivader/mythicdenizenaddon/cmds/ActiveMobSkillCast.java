package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.Types;
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
			if (!entry.hasObject(Types.caster.a()) && arg.matchesPrefix(Types.caster.a())
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.caster.a(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Types.skill.a()) && arg.matchesPrefix(Types.skill.a())) {
				entry.addObject(Types.skill.a(), arg.asElement());
			} else if (!entry.hasObject(Types.target.a()) && arg.matchesPrefix(Types.target.a())) {
				if (arg.getValue().toLowerCase().startsWith("l@")) {
					bool = false;
					entry.addObject(Types.target.a(), arg.asType(dLocation.class));
				} else {
					bool = true;
					entry.addObject(Types.target.a(), arg.asType(dEntity.class));
				}
			} else if (!entry.hasObject(Types.trigger.a()) && arg.matchesPrefix(Types.trigger.a())) {
				entry.addObject(Types.trigger.a(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.power.a()) && arg.matchesPrefix(Types.power.a())
					&& arg.matchesPrimitive(aH.PrimitiveType.Float)) {
				entry.addObject(Types.power.a(), arg.asElement());
			}
		}
		
		if (!entry.hasObject(Types.trigger.a())) {
			entry.addObject(Types.trigger.a(), new dEntity(((dActiveMob)entry.getdObject(Types.caster.a())).getEntity()));
		}
		if (!entry.hasObject(Types.power.a())) {
			entry.addObject(Types.power.a(), new Element("1"));
		}
	}
	
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		HashSet<Entity> etargets = new HashSet<Entity>();
		HashSet<Location> ltargets = new HashSet<Location>();
		if (bool) {
			etargets.add(((dEntity)entry.getdObject(Types.target.a())).getBukkitEntity());
		} else {
			ltargets.add(((dLocation)entry.getdObject(Types.target.a())));
		}
		ActiveMob caster = ((dActiveMob)entry.getdObject(Types.caster.a())).getActiveMob();
		Entity trigger = ((dEntity)entry.getdObject(Types.trigger.a())).getBukkitEntity();
		String skill = entry.getElement(Types.skill.a()).asString();
		float power = entry.getElement(Types.power.a()).asFloat();
		MythicMobs.inst().getAPIHelper().castSkill(BukkitAdapter.adapt(caster.getEntity()), skill, trigger, BukkitAdapter.adapt(caster.getLocation()), etargets, ltargets, power);
	}
}
