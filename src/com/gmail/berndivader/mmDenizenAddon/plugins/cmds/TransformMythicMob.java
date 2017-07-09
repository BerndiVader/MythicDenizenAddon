package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.TransformToMythicMob.Types;
import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class TransformMythicMob extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.activemob.name()) && arg.matchesPrefix(Types.activemob.name()) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.activemob.name(), arg.asType(dActiveMob.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am = ((dActiveMob)entry.getdObject(Types.activemob.name())).getActiveMob();
		if (am!=null) {
			Entity entity = transformToNormalEntity(am);
			if (entity!=null) {
				dEntity dentity = new dEntity(entity);
				entry.addObject(Types.entity.name(), dentity);
			} else {
				entry.addObject(Types.entity.name(), "dEntity@");
			}
		} else {
			entry.addObject(Types.entity.name(), "dEntity@");
		}
	}
	
	private static Entity transformToNormalEntity(ActiveMob am) {
		Entity entity = am.getEntity().getBukkitEntity();
		Location l = am.getEntity().getBukkitEntity().getLocation();
		l.setY(0);
		AbstractEntity d = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
		am.getEntity().getBukkitEntity().removeMetadata("Faction", MythicMobs.inst());
		unregisterActiveMob(am.getUniqueId());
		am.setEntity(d);
		d.remove();
		return entity;
	}
	
    private static void unregisterActiveMob(UUID uuid) {
		MythicMobs.inst().getMobManager().unregisterActiveMob(uuid);
    }
	
	
}
