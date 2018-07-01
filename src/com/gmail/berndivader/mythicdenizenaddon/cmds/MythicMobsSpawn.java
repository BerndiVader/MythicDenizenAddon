package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.Bukkit;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.Types;
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
			if (!entry.hasObject(Types.mobtype.a()) && arg.matchesPrefix(Types.mobtype.a())) {
				entry.addObject(Types.mobtype.a(), arg.asElement());
			} else if (!entry.hasObject(Types.location.a()) && arg.matchesArgumentType(dLocation.class)) {
				entry.addObject(Types.location.a(), arg.asType(dLocation.class));
			} else if (!entry.hasObject(Types.level.a()) 
					&& arg.matchesPrefix(Types.level.a()) && arg.matchesPrimitive(aH.PrimitiveType.Integer)) {
				entry.addObject(Types.level.a(), arg.asElement());
			} else if (!entry.hasObject(Types.world.a()) 
					&& arg.matchesPrefix(Types.world.a())) entry.addObject(Types.world.a(), arg.asElement());
			else arg.reportUnhandled();
		}
		
		if (!entry.hasObject(Types.mobtype.a())||!entry.hasObject(Types.location.a())) {
			throw new InvalidArgumentsException(Types.mobtype.a()+" & "+Types.location.a()+" is required!");
		}
		if (!entry.hasObject(Types.level.a())) {
			entry.addObject(Types.level.a(),new Element(1));
		}
	}
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String mobtype = entry.getElement(Types.mobtype.a()).asString();
		if (mobmanager.getMythicMob(mobtype)==null) {
			throw new CommandExecutionException("No MythicMobType found!");
		}
		int level = entry.getElement(Types.level.a()).asInt();
		dLocation loc = entry.getdObject(Types.location.a());
		String worldName = entry.hasObject(Types.world.a())?entry.getElement(Types.world.a()).asString():loc.getWorld().getName();
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
