package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.Bukkit;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class MythicMobsSpawn extends AbstractCommand {
	static MobManager mobmanager=MythicMobsAddon.mythicmobs.getMobManager();
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Statics.str_mobtype) && arg.matchesPrefix(Statics.str_mobtype)) {
				entry.addObject(Statics.str_mobtype, arg.asElement());
			} else if (!entry.hasObject(Statics.str_location) && arg.matchesArgumentType(dLocation.class)) {
				entry.addObject(Statics.str_location, arg.asType(dLocation.class));
			} else if (!entry.hasObject(Statics.str_level) 
					&& arg.matchesPrefix(Statics.str_level) && arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Statics.str_level, arg.asElement());
			} else if (!entry.hasObject(Statics.str_world) 
					&& arg.matchesPrefix(Statics.str_world)) entry.addObject(Statics.str_world, arg.asElement());
			else arg.reportUnhandled();
		}
		
		if (!entry.hasObject(Statics.str_mobtype)||!entry.hasObject(Statics.str_location)) {
			throw new InvalidArgumentsException(Statics.str_mobtype+" & "+Statics.str_location+" is required!");
		}
		if (!entry.hasObject(Statics.str_level)) {
			entry.addObject(Statics.str_level,new Element(1));
		}
	}
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String mobtype = entry.getElement(Statics.str_mobtype).asString();
		if (mobmanager.getMythicMob(mobtype)==null) {
			throw new CommandExecutionException("No MythicMobType found!");
		}
		int level = entry.getElement(Statics.str_level).asInt();
		dLocation loc = entry.getdObject(Statics.str_location);
		String worldName = entry.hasObject(Statics.str_world)?entry.getElement(Statics.str_world).asString():loc.getWorld().getName();
		AbstractLocation location = BukkitAdapter.adapt(loc);
		AbstractWorld world = BukkitAdapter.adapt(Bukkit.getServer().getWorld(worldName));
		if (location==null || world == null) return;
		AbstractLocation sl = new AbstractLocation(world, location.getX(), location.getY(), location.getZ());
		ActiveMob am;
		if ((am=mobmanager.spawnMob(mobtype,sl,level))!=null) {
			entry.addObject("activemob", new dActiveMob(am));
		} else {
			throw new CommandExecutionException("Failed to spawn MythicMob");
		}
	}
}
