package com.gmail.berndivader.mmDenizenAddon.plugins;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mmDenizenAddon.MythicDenizenPlugin;
import com.gmail.berndivader.mmDenizenAddon.Support;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.ActiveMobSkillCast;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.MythicMobsSpawn;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.PlayerSkillCast;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.SendSignal;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.dList;

public class MythicMobsAddon extends Support {
	
	@SuppressWarnings("unchecked")
	public MythicMobsAddon() {
		registerObjects(dMythicSpawner.class, dActiveMob.class);
		registerProperty(dEntityExt.class, dEntity.class);
		registerProperty(dWorldExt.class, dWorld.class);
		new MythicMobsSpawn().activate().as("mmspawnmob").withOptions("- mmspawnmob [mobtype:string] [location] (world:string) (level:integer)", 2);
		new ActiveMobSkillCast().activate().as("mmcastmob").withOptions("- mmcastmob [caster:dActiveMob] [target:dEntity||dLocation] [skill:string] (trigger:dEntity) (power:float)",3);
		new SendSignal().activate().as("mmsignal").withOptions("- mmsignal [activemob:dActiveMob] [singal:string] (trigger:dEntity)", 2);
		new PlayerSkillCast().activate().as("mmplayercast").withOptions("- mmplayercast [player:dPlayer] [skill:string] [target:dEntity||dLocation] (trigger:dEntity) (repeat:integer) (delay:integer)", 3);
	}

	public static boolean isActiveMob(UUID uuid) {
		return MythicMobs.inst().getMobManager().isActiveMob(uuid);
	}
	
	public static boolean isMythicMob(String type) {
		return MythicMobs.inst().getMobManager().getMythicMob(type)!=null;
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

	public static Entity getOwner(ActiveMob am) {
		if (am.getOwner().isPresent()) {
			UUID uuid = am.getOwner().get();
			return dEntity.getEntityForID(uuid);
		}
		return null;
	}

	public static Entity getLastAggro(ActiveMob am) {
		if (am.getLastAggroCause() != null) {
			return am.getLastAggroCause().getBukkitEntity();
		};
		return null;
	}

	public static Entity getTopTarget(ActiveMob am) {
		if (am.hasThreatTable()) {
			return am.getThreatTable().getTopThreatHolder().getBukkitEntity();
		} else if (am.hasTarget()) {
			return am.getEntity().getTarget().getBukkitEntity();
		}
		return null;
	}

	public static boolean hasTarget(ActiveMob am) {
		return (am.hasThreatTable() || am.hasTarget())?true:false;
	}

	public static void setCustomName(ActiveMob am, String name) {
		am.getEntity().getBukkitEntity().setCustomName(name);
	}

	public static void setTarget(ActiveMob am, Entity target) {
		if (am.hasThreatTable() && (target instanceof LivingEntity)) {
			double h = am.getThreatTable().getTopTargetThreat();
			MythicMobs.inst().getAPIHelper().addThreat(am.getEntity().getBukkitEntity(), (LivingEntity)target, h+1);
		} else {
			am.setTarget(BukkitAdapter.adapt(target));
		}
	}

	public static dList allActiveMobs(World world) {
		dList ams = new dList();
		for (ActiveMob am : MythicMobs.inst().getMobManager().getActiveMobs()) {
			if (am.getLocation().getWorld().getName().equals(world.getName())) {
				ams.add(new dActiveMob(am).identify());
			}
		}
		return ams;
	}

	public static boolean isMythicSpawner(String uniqueName) {
		return MythicMobs.inst().getSpawnerManager().getSpawnerByName(uniqueName) != null;
	}

	public static MythicSpawner getMythicSpawner(String uniqueName) {
		return MythicMobs.inst().getSpawnerManager().getSpawnerByName(uniqueName);
	}

	public static dList allMythicSpawners(World world) {
		dList mss = new dList();
		for (MythicSpawner ms : MythicMobs.inst().getSpawnerManager().listSpawners) {
			if (ms.getLocation().getWorld().getName().equals(world.getName())) {
				mss.add(new dMythicSpawner(ms).identify());
			}
		}
		return mss;
	}

	public static dList getActiveMobsFromSpawner(MythicSpawner ms) {
		dList ams = new dList();
		for (UUID uuid : ms.getAssociatedMobs()) {
			if (MythicMobs.inst().getMobManager().getActiveMob(uuid).isPresent()) {
				ams.add(new dActiveMob(MythicMobs.inst().getMobManager().getActiveMob(uuid).get()).identify());
			}
		}
		return ams;
	}

	public static void attachMobToSpawner(MythicSpawner ms, dActiveMob activeMob) {
		ActiveMob am = activeMob.am;
		am.setSpawner(ms);
		if (!ms.getAssociatedMobs().contains(am.getUniqueId())) ms.trackMob(am);
	}
}
