package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.Types;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class MythicMobSkillCast extends AbstractCommand {
	private boolean bool;
	private static BukkitAPIHelper mmapi=MythicMobs.inst().getAPIHelper();
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.caster.a()) && arg.matchesPrefix(Types.caster.a())) {
				if (arg.matchesArgumentType(dEntity.class)) {
					entry.addObject(Types.caster.a(),arg.asType(dEntity.class));
				} else if (arg.matchesArgumentType(dPlayer.class)) {
					entry.addObject(Types.caster.a(),arg.asType(dPlayer.class).getDenizenEntity());
				} else if (arg.matchesArgumentType(dActiveMob.class)) {
					entry.addObject(Types.caster.a(),new dEntity(arg.asType(dActiveMob.class).getEntity()));
				}
			} else if (!entry.hasObject(Types.skill.a()) && arg.matchesPrefix(Types.skill.a())) {
				entry.addObject(Types.skill.name(), arg.asElement());
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
			} else if (!entry.hasObject(Types.repeat.a()) && arg.matchesPrefix(Types.repeat.a()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.repeat.a(), arg.asElement());
			} else if (!entry.hasObject(Types.delay.a()) && arg.matchesPrefix(Types.delay.a()) 
					&& arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.delay.a(), arg.asElement());
			}
		}
		if (!entry.hasObject(Types.trigger.a())) {
			entry.addObject(Types.trigger.a(), entry.getdObject(Types.caster.a()));
		}
		if (!entry.hasObject(Types.power.a())) {
			entry.addObject(Types.power.a(), new Element("1"));
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
		HashSet<Entity> etargets=new HashSet<Entity>();
		HashSet<Location> ltargets=new HashSet<Location>();
		if (bool) {
			Entity e=null;
			if ((e=((dEntity)entry.getdObject(Types.target.a())).getBukkitEntity())!=null) etargets.add(e);
		} else {
			ltargets.add(((dLocation)entry.getdObject(Types.target.a())));
		}
		Entity caster=((dEntity)entry.getdObject(Types.caster.a())).getBukkitEntity();
		Entity trigger = ((dEntity)entry.getdObject(Types.trigger.a())).getBukkitEntity();
		String skill = entry.getElement(Types.skill.a()).asString();
		float power = entry.getElement(Types.power.a()).asFloat();
		int ttimer = entry.getElement(Types.repeat.a()).asInt();
		long tdelay = entry.getElement(Types.delay.a()).asLong();
    	new BukkitRunnable() {
    		int timer = ttimer;
    		public void run() {
    			if (timer==-1) {
    				this.cancel();
    			} else {
    				mmapi.castSkill(caster,skill,trigger,caster.getLocation(),etargets,ltargets,power);
    				timer--;
    			}
            } 
        }.runTaskTimer(MythicDenizenPlugin.inst(), 0, tdelay);
	}
}
