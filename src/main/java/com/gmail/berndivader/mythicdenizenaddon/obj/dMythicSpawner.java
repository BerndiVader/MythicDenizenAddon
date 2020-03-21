package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import io.lumine.xikage.mythicmobs.utils.numbers.RandomInt;

public 
class 
dMythicSpawner 
implements 
ObjectTag, 
Adjustable 
{
	static String id="mythicspawner@";
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
		ElementTag val = m.getValue();
		switch(m.getName().toLowerCase()) {
		case "activate":
			if (val.asBoolean()) {
				ms.ActivateSpawner();
			} else {
				ms.Disable();
			}
			break;
		case "remainingcooldown":
			ms.setRemainingCooldownSeconds(val.asLong());
			break;
		case "cooldown":
			ms.setCooldownSeconds(val.asInt());
			break;
		case "remainingwarmup":
			ms.setRemainingWarmupSeconds(val.asLong());
			break;
		case "warmup":
			ms.setWarmupSeconds(val.asInt());
			break;
		case "mobtype":
			if (MythicMobsAddon.isMythicMob(val.asString())) {
				ms.setType(val.asString());
			}
			break;
		case "moblevel":
			ms.setMobLevel(new RandomInt(val.asInt()));
			break;
		case "spawn":
			ms.Spawn();
			break;
		case "attachmob":
			MythicMobsAddon.attachMobToSpawner(ms,val.asType(dActiveMob.class,m.context));
			break;
		}
	}
	
	@Override
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		if (a.startsWith("location")) {
			return new LocationTag(BukkitAdapter.adapt(ms.getLocation())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("world")) {
			return new WorldTag(BukkitAdapter.adapt(ms.getLocation().getWorld())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("allactivemobs")) {
			return MythicMobsAddon.getActiveMobsFromSpawner(ms).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobtype")) {
			return new ElementTag(ms.getTypeName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("moblevel")) {
			return new ElementTag(ms.getMobLevel().get()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("cooldown")) {
			return new ElementTag(ms.getCooldownSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("remainingcooldown")) {
			return new ElementTag(ms.getRemainingCooldownSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("warmup")) {
			return new ElementTag(ms.getWarmupSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("remainingwarmup")) {
			return new ElementTag(ms.getRemainingWarmupSeconds()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobamount")) {
			return new ElementTag(ms.getNumberOfMobs()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("maxmobamount")) {
			return new ElementTag(ms.getMaxMobs().get()).getAttribute(a.fulfill(1));
		}
		return new ElementTag(identify()).getAttribute(a);
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
		return this.ms!=null?id+this.ms.getName():null;
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
	public ObjectTag setPrefix(String string) {
		this.prefix = string;
		return this;
	}
	
	@Override
	public String toString() {
		return this.identify();
	}
	
    @Fetchable("mythicspawner")
    public static dMythicSpawner valueOf(String uniqueName, TagContext context) {
        if (uniqueName == null) {
            return null;
        }
        try {
            uniqueName = uniqueName.replace(id,"");
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
