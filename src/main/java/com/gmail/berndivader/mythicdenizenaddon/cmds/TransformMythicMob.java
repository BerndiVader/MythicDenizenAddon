package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public 
class 
TransformMythicMob
extends
AbstractCommand 
{
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_activemob) && arg.matchesPrefix(Statics.str_activemob) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Statics.str_activemob, arg.asType(dActiveMob.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am=((dActiveMob)entry.getObjectTag(Statics.str_activemob)).getActiveMob();
		if (am!=null) {
			Entity entity = transformToNormalEntity(am);
			if (entity!=null) {
				EntityTag dentity = new EntityTag(entity);
				entry.addObject(Statics.str_entity,dentity);
			} else {
				throw new CommandExecutionException("Failed to get Entity from MythicMob");
			}
		}
	}
	
	private static Entity transformToNormalEntity(ActiveMob am) {
		Entity entity=am.getEntity().getBukkitEntity();
		entity.removeMetadata("Faction", MythicMobsAddon.mythicmobs);
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
    	if (o!=null) MythicMobsAddon.mythicmobs.getMobManager().unregisterActiveMob(((ActiveMob)o));
    }
    private static void ureg(UUID o) {
    	if (o!=null) MythicMobsAddon.mythicmobs.getMobManager().unregisterActiveMob(((UUID)o));
    }
	
	
}
