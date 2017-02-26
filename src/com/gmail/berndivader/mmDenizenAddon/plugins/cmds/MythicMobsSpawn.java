package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import org.bukkit.Bukkit;

import com.gmail.berndivader.mmDenizenAddon.plugins.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class MythicMobsSpawn extends AbstractCommand {

	private enum Types {
		mobtype,
		location,
		world,
		level
	}
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.mobtype.name()) && arg.matchesPrefix(Types.mobtype.name())) {
				entry.addObject(Types.mobtype.name(), arg.asElement());
			} else if (!entry.hasObject(Types.location.name()) && arg.matchesArgumentType(dLocation.class)) {
				entry.addObject(Types.location.name(), arg.asType(dLocation.class));
			} else if (!entry.hasObject(Types.level.name()) 
					&& arg.matchesPrefix(Types.level.name()) && arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.level.name(), arg.asElement());
			} else if (!entry.hasObject(Types.world.name()) 
					&& arg.matchesPrefix(Types.world.name())) entry.addObject(Types.world.name(), arg.asElement());
			else arg.reportUnhandled();
		}
		
		if (!entry.hasObject(Types.mobtype.name()) 
				|| !entry.hasObject(Types.location.name())) Bukkit.getLogger().warning("Mobtype and location required!");
		if (!entry.hasObject(Types.level.name())) entry.defaultObject(Types.level.name(), new Element(1));
	}

	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String mobtype = entry.getElement(Types.mobtype.name()).asString();
		if (MythicMobs.inst().getAPIHelper().getMythicMob(mobtype) == null) return;
		int level = entry.getElement(Types.level.name()).asInt();
		dLocation loc = entry.getdObject(Types.location.name());
		String worldName = entry.hasObject(Types.world.name())?entry.getElement(Types.world.name()).asString():loc.getWorld().getName();
		AbstractLocation location = BukkitAdapter.adapt(loc);
		AbstractWorld world = BukkitAdapter.adapt(Bukkit.getServer().getWorld(worldName));
		if (location==null || world == null) return;
		AbstractLocation sl = new AbstractLocation(world, location.getX(), location.getY()+0.5, location.getZ());
		ActiveMob am;
		if ((am=MythicMobs.inst().getMobManager().spawnMob(mobtype, sl, level))==null) return;
		entry.addObject("activemob", new dActiveMob(am));
	}
}
