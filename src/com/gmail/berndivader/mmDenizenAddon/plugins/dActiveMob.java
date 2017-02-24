package com.gmail.berndivader.mmDenizenAddon.plugins;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dActiveMob implements dObject, Adjustable {
	
	private String prefix;
	ActiveMob am;
	Entity entity;

	public dActiveMob(ActiveMob activeMob) {
		if (activeMob==null) return;
		this.am = activeMob;
		this.entity = BukkitAdapter.adapt(activeMob.getEntity());
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dActiveMob valueOf(String uuid) {
		return valueOf(uuid, null);
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
		if (m.matches("gcd") && m.requireInteger()) {
			this.am.setGlobalCooldown(val.asInt());
		} else if (m.matches("remove")) {
			MythicMobsAddon.removeSelf(this.am);
		} else if (m.matches("displayname")) {
			MythicMobsAddon.setCustomName(am, val.asString());
		} else if (m.matches("owner")) {
			am.setOwner(val.asType(dEntity.class).getUUID());
		} else if (m.matches("target")) {
			MythicMobsAddon.setTarget(am, val.asType(dEntity.class).getBukkitEntity());
		} else if (m.matches("faction")) {
			am.setFaction(val.asString());
		} else if (m.matches("stance")) {
			am.setStance(val.asString());
		} else if (m.matches("level")) {
			am.setLevel(val.asInt());
		} else if (m.matches("playerkills")) {
			am.importPlayerKills(val.asInt());
		}
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		if (a.startsWith("isdead")) {
			return new Element(MythicMobsAddon.isDead(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasthreattable")) {
			return new Element(MythicMobsAddon.hasThreatTable(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hastarget")) {
			return new Element(MythicMobsAddon.hasTarget(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasmythicspawner")) {
			return new Element(MythicMobsAddon.hasMythicSpawner(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobtype")) {
			return new Element(am.getType().getInternalName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("displayname")) {
			return new Element(am.getType().getDisplayName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("location")) {
			return new dLocation(BukkitAdapter.adapt(am.getLocation())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("world")) {
			return new dWorld(BukkitAdapter.adapt(am.getLocation().getWorld())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("owner")) {
			return new dEntity(MythicMobsAddon.getOwner(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastaggro")) {
			return new dEntity(MythicMobsAddon.getLastAggro(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("toptarget")) {
			return new dEntity(MythicMobsAddon.getTopTarget(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("uuid")) {
			return new Element(am.getUniqueId().toString()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("health")) {
			return new Element(am.getEntity().getHealth()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("maxhealth")) {
			return new Element(am.getEntity().getMaxHealth()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("faction")) {
			return new Element(am.getFaction()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("stance")) {
			return new Element(am.getStance()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("level")) {
			return new Element(am.getLevel()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("playerkills")) {
			return new Element(am.getPlayerKills()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastsignal")) {
			return new Element(am.getLastSignal()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("type")) {
			return new Element("ActiveMob").getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a);
	}

	@Override
	public void applyProperty(Mechanism mech) {
		Bukkit.getLogger().warning("Cannot apply properties to a ActiveMob.");
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "ActiveMob";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return "activemob@" + this.am.getUniqueId();
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

    @Fetchable("activemob")
    public static dActiveMob valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("activemob@", "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsAddon.isActiveMob(uuid)) {
                return null;
            }
            return new dActiveMob(MythicMobsAddon.getActiveMob(dEntity.getEntityForID(uuid)));
        }
        catch (Exception e) {
            return null;
        }
    }
}
