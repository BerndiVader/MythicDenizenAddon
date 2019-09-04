package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.UUID;

import org.bukkit.entity.Entity;

import com.denizenscript.denizen.objects.EntityTag;
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

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public 
class 
dActiveMob 
implements
ObjectTag, 
Adjustable 
{
	
	static String id="activemob@";
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
		ElementTag val=m.getValue();
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
			am.setOwner(val.asType(EntityTag.class).getUUID());
			break;
		case "target":
			MythicMobsAddon.setTarget(am, val.asType(EntityTag.class).getBukkitEntity());
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
				ElementTag entity=new ElementTag(parse[0]);
				ElementTag value=new ElementTag(parse[1]);
				if (entity.matchesType(EntityTag.class) && value.isDouble()) MythicMobsAddon.modThreatOfEntity(am, entity.asType(EntityTag.class), value.asDouble(), m.getName());
			}
			break;
		case "clearthreat":
		case "zapthreat":
			am.getThreatTable().getAllThreatTargets().clear();
			break;
		case "removethreat":
		case "delththreat":
			if (val.matchesType(EntityTag.class)) MythicMobsAddon.removeThreatOfEntity(am, val.asType(EntityTag.class));
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
			if (m.requireObject(EntityTag.class)) am.getImmunityTable().setCooldown(BukkitAdapter.adapt(val.asType(EntityTag.class).getBukkitEntity()));
			break;
		}
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		if (a.startsWith("isdead")) {
			return new ElementTag(MythicMobsAddon.isDead(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("entity")) {
			return new EntityTag(this.am.getEntity().getBukkitEntity()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasthreattable")) {
			return new ElementTag(MythicMobsAddon.hasThreatTable(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("threattable")) {
			return MythicMobsAddon.getThreatTable(am).getAttribute(a.fulfill(1));
		} else if (a.startsWith("threatvalueof") && a.hasContext(1)) {
			return new ElementTag(MythicMobsAddon.getThreatValueOf(am,EntityTag.valueOf(a.getContext(1)))).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hastarget")) {
			return new ElementTag(MythicMobsAddon.hasTarget(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasmythicspawner")) {
			return new ElementTag(MythicMobsAddon.hasMythicSpawner(entity)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mythicspawner")) {
			return new dMythicSpawner(am.getSpawner()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("mobtype")) {
			return new ElementTag(am.getType().getInternalName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("displayname")) {
			return new ElementTag(am.getType().getDisplayName().get()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("location")) {
			return new LocationTag(BukkitAdapter.adapt(am.getLocation())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("world")) {
			return new WorldTag(BukkitAdapter.adapt(am.getLocation().getWorld())).getAttribute(a.fulfill(1));
		} else if (a.startsWith("owner")) {
			return new EntityTag(MythicMobsAddon.getOwner(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastaggro")) {
			return new EntityTag(MythicMobsAddon.getLastAggro(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("toptarget")) {
			return new EntityTag(MythicMobsAddon.getTopTarget(am)).getAttribute(a.fulfill(1));
		} else if (a.startsWith("uuid")) {
			return new ElementTag(am.getUniqueId().toString()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("health")) {
			return new ElementTag(am.getEntity().getHealth()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("maxhealth")) {
			return new ElementTag(am.getEntity().getMaxHealth()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("faction")) {
			return new ElementTag(am.getFaction()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("stance")) {
			return new ElementTag(am.getStance()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("level")) {
			return new ElementTag(am.getLevel()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("playerkills")) {
			return new ElementTag(am.getPlayerKills()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastsignal")) {
			return new ElementTag(am.getLastSignal()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("type")) {
			return new ElementTag("ActiveMob").getAttribute(a.fulfill(1));
		} else if (a.startsWith("damage")) {
			return new ElementTag(am.getDamage()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("power")) {
			return new ElementTag(am.getPower()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("lastdamageskillamount")) {
			return new ElementTag(am.getLastDamageSkillAmount()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("hasimmunitytable")) {
				return new ElementTag(this.am.hasImmunityTable()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("isonimmunitycooldown") && a.hasContext(1)) {
			AbstractEntity ae = BukkitAdapter.adapt(EntityTag.valueOf(a.getContext(1)).getBukkitEntity());
			return new ElementTag(am.getImmunityTable().onCooldown(ae)).getAttribute(a.fulfill(1));
		}
		return new ElementTag(identify()).getAttribute(a);
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
		return this.am!=null?id+this.am.getUniqueId():null;
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

    @Fetchable("activemob")
    public static dActiveMob valueOf(String string, TagContext context) {
        if (string==null) return null;
        try {
            string = string.replace(id, "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsAddon.isActiveMob(uuid)) {
                return null;
            }
            return new dActiveMob(MythicMobsAddon.getActiveMob(EntityTag.getEntityForID(uuid)));
        }
        catch (Exception e) {
            return null;
        }
    }

}