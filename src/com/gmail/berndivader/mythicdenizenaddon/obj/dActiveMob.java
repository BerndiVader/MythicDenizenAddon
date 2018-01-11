package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.UUID;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
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
	private ActiveMob am;
	private Entity entity;

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dActiveMob valueOf(String uuid) {
		return valueOf(uuid, null);
	}
	
	public dActiveMob(ActiveMob activeMob) {
		if (activeMob==null) return;
		this.am = activeMob;
		this.entity = BukkitAdapter.adapt(activeMob.getEntity());
	}
	
	public ActiveMob getActiveMob() {
		return this.am;
	}
	public Entity getEntity() {
		return this.entity;
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element val=m.getValue();
		switch(m.getName().toLowerCase()) {
		case "gcd":
			this.am.setGlobalCooldown(val.asInt());
			break;
		case "remove":
			MythicMobsAddon.removeSelf(this.am);
			break;
		case "displayname":
			MythicMobsAddon.setCustomName(am,val.asString());
			break;
		case "owner":
			am.setOwner(val.asType(dEntity.class).getUUID());
			break;
		case "target":
			MythicMobsAddon.setTarget(am, val.asType(dEntity.class).getBukkitEntity());
			break;
		case "faction":
			am.setFaction(val.asString());
			break;
		case "stance":
			am.setStance(val.asString());
			break;
		case "level":
			am.setLevel(val.asInt());
			break;
		case "playerkills":
			am.importPlayerKills(val.asInt());
			break;
		case "health":
			am.getEntity().setHealth(val.asDouble());
			break;
		case "maxhealth":
			am.getEntity().setMaxHealth(val.asDouble());
			break;
		case "incthreat":
		case "decthreat":
			String[]parse=val.asString().split("\\|");
			if(parse.length>1) {
				Element entity=new Element(parse[0]);
				Element value=new Element(parse[1]);
				if (entity.matchesType(dEntity.class) && value.isDouble()) MythicMobsAddon.modThreatOfEntity(am, entity.asType(dEntity.class), value.asDouble(), m.getName());
			}
			break;
		case "clearthreat":
			am.getThreatTable().getAllThreatTargets().clear();
			break;
		case "removethreat":
			if (val.matchesType(dEntity.class)) MythicMobsAddon.removeThreatOfEntity(am, val.asType(dEntity.class));
			break;
		case "newtargetthreattable":
			am.getThreatTable().clearTarget();
			am.getThreatTable().dropCombat();
			am.getThreatTable().targetHighestThreat();
			break;
		case "getnewtarget":
			am.getNewTarget();
			break;
		case "setimmunitycooldown":
			if (m.requireObject(dEntity.class)) am.getImmunityTable().setCooldown(BukkitAdapter.adapt(val.asType(dEntity.class).getBukkitEntity()));
			break;
		}
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return "<NONE>";
		if (a.startsWith("isdead")) {
			return new Element(MythicMobsAddon.isDead(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("entity")) {
			return new dEntity(this.am.getEntity().getBukkitEntity()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasthreattable")) {
			return new Element(MythicMobsAddon.hasThreatTable(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("threattable")) {
			return MythicMobsAddon.getThreatTable(am).getAttribute(a.fulfill(1));
		} else if (a.startsWith("threatvalueof") && a.hasContext(1)) {
			return new Element(MythicMobsAddon.getThreatValueOf(am, dEntity.valueOf(a.getContext(1)))).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hastarget")) {
			return new Element(MythicMobsAddon.hasTarget(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasmythicspawner")) {
			return new Element(MythicMobsAddon.hasMythicSpawner(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mythicspawner")) {
			return new dMythicSpawner(am.getSpawner()).getAttribute(a.fulfill(1));
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
		} else if (a.startsWith("damage")) {
			return new Element(am.getDamage()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("power")) {
			return new Element(am.getPower()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastdamageskillamount")) {
			return new Element(am.getLastDamageSkillAmount()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasimmunitytable")) {
				return new Element(this.am.hasImmunityTable()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("isonimmunitycooldown") && a.hasContext(1)) {
			AbstractEntity ae = BukkitAdapter.adapt(dEntity.valueOf(a.getContext(1)).getBukkitEntity());
			return new Element(am.getImmunityTable().onCooldown(ae)).getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a);
	}

	@Override
	public void applyProperty(Mechanism mech) {
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
        if (string==null) return null;
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
