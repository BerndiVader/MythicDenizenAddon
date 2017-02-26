package com.gmail.berndivader.mmDenizenAddon.plugins;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicSpawner implements dObject, Adjustable {
	private String prefix;
	MythicSpawner ms;

	public dMythicSpawner(MythicSpawner mythicSpawner) {
		this.ms = mythicSpawner;
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dMythicSpawner valueOf(String name) {
		return valueOf(name, null);
	}

	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
		if (m.matches("activate") && m.requireBoolean()) {
			if (val.asBoolean()) {
				ms.ActivateSpawner();
			} else {
				ms.Disable();
			}
		} else if (m.matches("remainingcooldown") && m.requireInteger()) {
			ms.setRemainingCooldownSeconds(val.asLong());
		} else if (m.matches("cooldown")&& m.requireInteger()) {
			ms.setCooldownSeconds(val.asInt());
		} else if (m.matches("remainingwarmup")&& m.requireInteger()) {
			ms.setRemainingWarmupSeconds(val.asLong());
		} else if (m.matches("warmup")&& m.requireInteger()) {
			ms.setRemainingWarmupSeconds(val.asInt());
		} else if (m.matches("mobtype")) {
			if (MythicMobsAddon.isMythicMob(val.asString())) {
				ms.setType(val.asString());
			}
		} else if (m.matches("moblevel")&& m.requireInteger()) {
			ms.setMobLevel(val.asInt());
		} else if (m.matches("spawn")) {
			ms.Spawn();
		} else if (m.matches("attachmob")&& m.requireObject(dActiveMob.class)) {
			MythicMobsAddon.attachMobToSpawner(ms, val.asType(dActiveMob.class));
		}
	}
	
	@Override
	public String getAttribute(Attribute a) {
		if (a.startsWith("location")) {
			return new dLocation(BukkitAdapter.adapt(ms.getLocation())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("world")) {
			return new dWorld(BukkitAdapter.adapt(ms.getLocation().getWorld())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("allactivemobs")) {
			return MythicMobsAddon.getActiveMobsFromSpawner(ms).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobtype")) {
			return new Element(ms.getTypeName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("moblevel")) {
			return new Element(ms.getMobLevel()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("cooldown")) {
			return new Element(ms.getCooldownSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("remainingcooldown")) {
			return new Element(ms.getRemainingCooldownSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("warmup")) {
			return new Element(ms.getWarmupSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("remainingwarmup")) {
			return new Element(ms.getRemainingWarmupSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobamount")) {
			return new Element(ms.getNumberOfMobs()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("maxmobamount")) {
			return new Element(ms.getMaxMobs()).getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a);
	}
	
	@Override
	public void applyProperty(Mechanism arg0) {
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicSpawner";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return "mythicspawner@" + this.ms.getName();
	}

	@Override
	public String identifySimple() {
		return identify();
	}

	@Override
	public boolean isUnique() {
		return true;
	}
	
	@Override
	public dObject setPrefix(String string) {
		this.prefix = string;
		return this;
	}
	
    @Fetchable("mythicspawner")
    public static dMythicSpawner valueOf(String uniqueName, TagContext context) {
        if (uniqueName == null) {
            return null;
        }
        try {
            uniqueName = uniqueName.replace("mythicspawner@", "");
            if (!MythicMobsAddon.isMythicSpawner(uniqueName)) {
                return null;
            }
            return new dMythicSpawner(MythicMobsAddon.getMythicSpawner(uniqueName));
        }
        catch (Exception e) {
            return null;
        }
    }
}
