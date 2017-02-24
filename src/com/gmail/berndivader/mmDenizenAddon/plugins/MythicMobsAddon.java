package com.gmail.berndivader.mmDenizenAddon.plugins;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmDenizenAddon.MythicDenizenPlugin;
import com.gmail.berndivader.mmDenizenAddon.Support;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;

public class MythicMobsAddon extends Support {
	
	@SuppressWarnings("unchecked")
	public MythicMobsAddon() {
		registerObjects(dActiveMob.class);
		registerProperty(dActiveMobExt.class, dEntity.class);
	}

	public static boolean isActiveMob(UUID uuid) {
		return MythicMobs.inst().getMobManager().isActiveMob(uuid);
	}

	public static boolean isActiveMob(Entity e) {
		return MythicMobs.inst().getMobManager().isActiveMob(BukkitAdapter.adapt(e));
	}

	public static ActiveMob getActiveMob(Entity e) {
		return MythicMobs.inst().getMobManager().getMythicMobInstance(e);
	}
	
	public static boolean removeSelf(ActiveMob am) {
		if (!am.isUsingDamageSkill()) {
			am.setDead();
			am.getEntity().remove();
		} else {
	    	new BukkitRunnable() {
	            ActiveMob ram = am;
	    		public void run() {
	    			if (ram.isDead() || !ram.isUsingDamageSkill()) {
	    				ram.setDead();
	    				ram.getEntity().remove();
	    				this.cancel();
	    			}
	            }
	        }.runTaskTimer(MythicDenizenPlugin.inst(), 1, 1);
		}
		return true;
	}

	public static boolean isDead(Entity e) {
		ActiveMob am;
		if ((am=MythicMobs.inst().getAPIHelper().getMythicMobInstance(e))!=null) {
			return am.isDead();
		}
		return false;
	}

	public static boolean hasThreatTable(Entity e) {
		ActiveMob am;
		if ((am=MythicMobs.inst().getAPIHelper().getMythicMobInstance(e))!=null) {
			return am.hasThreatTable();
		}
		return false;
	}

	public static boolean hasMythicSpawner(Entity e) {
		ActiveMob am;
		if ((am=MythicMobs.inst().getAPIHelper().getMythicMobInstance(e))!=null) {
			return am.getSpawner()!=null;
		}
		return false;
	}
}
