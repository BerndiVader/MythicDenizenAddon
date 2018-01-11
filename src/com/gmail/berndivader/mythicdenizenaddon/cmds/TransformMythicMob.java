package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.gmail.berndivader.mythicdenizenaddon.Types;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

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
			if (!entry.hasObject(Types.activemob.a()) && arg.matchesPrefix(Types.activemob.a()) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.activemob.a(), arg.asType(dActiveMob.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am = ((dActiveMob)entry.getdObject(Types.activemob.a())).getActiveMob();
		if (am!=null) {
			Entity entity = transformToNormalEntity(am);
			if (entity!=null) {
				dEntity dentity = new dEntity(entity);
				entry.addObject(Types.entity.a(),dentity);
			} else {
				entry.addObject(Types.entity.a(),"e@");
			}
		} else {
			entry.addObject(Types.entity.a(),"e@");
		}
	}
	
	private static Entity transformToNormalEntity(ActiveMob am) {
		Entity entity=am.getEntity().getBukkitEntity();
		entity.removeMetadata("Faction", MythicMobs.inst());
		Location l=am.getEntity().getBukkitEntity().getLocation().clone();
		am.setDead();
		ureg(am.getUniqueId());
		l.setY(0);
		AbstractEntity d=BukkitAdapter.adapt(l.getWorld().spawnEntity(l,EntityType.BAT));
		am.setEntity(d);
		ureg(am);
		MythicMobs.inst().getMobManager().getVoidList().remove(entity.getUniqueId());
		d.remove();
		return entity;
	}
	
    private static void ureg(ActiveMob o) {
    	if (o!=null) MythicMobs.inst().getMobManager().unregisterActiveMob(((ActiveMob)o));
    }
    private static void ureg(UUID o) {
    	if (o!=null) MythicMobs.inst().getMobManager().unregisterActiveMob(((UUID)o));
    }
	
	
}
