package com.gmail.berndivader.mythicdenizenaddon.obj;

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
import org.bukkit.entity.Entity;

import java.util.UUID;

public class dActiveMob implements ObjectTag, Adjustable {

    static String id = "activemob@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dActiveMob valueOf(String uuid) {
        return valueOf(uuid, null);
    }

    @Fetchable("activemob")
    public static dActiveMob valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }

        try {
            string = string.replace(id, "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsAddon.isActiveMob(uuid)) {
                return null;
            }
            return new dActiveMob(MythicMobsAddon.getActiveMob(EntityTag.getEntityForID(uuid)));
        } catch (Exception e) {
            return null;
        }
    }

    private String prefix;
    private ActiveMob am;
    private Entity entity;

    public dActiveMob(ActiveMob activeMob) {
        if (activeMob == null) {
            return;
        }
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
    public String getObjectType() {
        return "ActiveMob";
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>'";
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public ObjectTag setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String identify() {
        return this.am != null ? id + this.am.getUniqueId() : null;
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
    public String toString() {
        return this.identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("isdead")) {
            return new ElementTag(MythicMobsAddon.isDead(entity)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("entity")) {
            return new EntityTag(this.am.getEntity().getBukkitEntity()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("hasthreattable")) {
            return new ElementTag(MythicMobsAddon.hasThreatTable(entity)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("threattable")) {
            return MythicMobsAddon.getThreatTable(am).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("threatvalueof") && attribute.hasContext(1)) {
            return new ElementTag(MythicMobsAddon.getThreatValueOf(am, EntityTag.valueOf(attribute.getContext(1)))).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("hastarget")) {
            return new ElementTag(MythicMobsAddon.hasTarget(am)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("hasmythicspawner")) {
            return new ElementTag(MythicMobsAddon.hasMythicSpawner(entity)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("mythicspawner")) {
            return new dMythicSpawner(am.getSpawner()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("mobtype")) {
            return new ElementTag(am.getType().getInternalName()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("displayname")) {
            return new ElementTag(am.getType().getDisplayName().get()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("location")) {
            return new LocationTag(BukkitAdapter.adapt(am.getLocation())).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("world")) {
            return new WorldTag(BukkitAdapter.adapt(am.getLocation().getWorld())).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("owner")) {
            return new EntityTag(MythicMobsAddon.getOwner(am)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("lastaggro")) {
            return new EntityTag(MythicMobsAddon.getLastAggro(am)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("toptarget")) {
            return new EntityTag(MythicMobsAddon.getTopTarget(am)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("uuid")) {
            return new ElementTag(am.getUniqueId().toString()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("health")) {
            return new ElementTag(am.getEntity().getHealth()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("maxhealth")) {
            return new ElementTag(am.getEntity().getMaxHealth()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("faction")) {
            return new ElementTag(am.getFaction()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("stance")) {
            return new ElementTag(am.getStance()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("level")) {
            return new ElementTag(am.getLevel()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("playerkills")) {
            return new ElementTag(am.getPlayerKills()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("lastsignal")) {
            return new ElementTag(am.getLastSignal()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("type")) {
            return new ElementTag("ActiveMob").getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("damage")) {
            return new ElementTag(am.getDamage()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("power")) {
            return new ElementTag(am.getPower()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("lastdamageskillamount")) {
            return new ElementTag(am.getLastDamageSkillAmount()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("hasimmunitytable")) {
            return new ElementTag(this.am.hasImmunityTable()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("isonimmunitycooldown") && attribute.hasContext(1)) {
            AbstractEntity ae = BukkitAdapter.adapt(EntityTag.valueOf(attribute.getContext(1)).getBukkitEntity());
            return new ElementTag(am.getImmunityTable().onCooldown(ae)).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag val = mechanism.getValue();
        switch (mechanism.getName().toLowerCase()) {
            case "gcd":
                this.am.setGlobalCooldown(val.asInt());
                break;
            case "remove":
                MythicMobsAddon.removeSelf(this.am);
                break;
            case "displayname":
                MythicMobsAddon.setCustomName(am, val.asString());
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
                String[] parse = val.asString().split("\\|");
                if (parse.length > 1) {
                    ElementTag entity = new ElementTag(parse[0]);
                    ElementTag value = new ElementTag(parse[1]);
                    if (entity.matchesType(EntityTag.class) && value.isDouble()) {
                        MythicMobsAddon.modThreatOfEntity(am, entity.asType(EntityTag.class), value.asDouble(), mechanism.getName());
                    }
                }
                break;
            case "clearthreat":
            case "zapthreat":
                am.getThreatTable().getAllThreatTargets().clear();
                break;
            case "removethreat":
            case "delththreat":
                if (val.matchesType(EntityTag.class)) {
                    MythicMobsAddon.removeThreatOfEntity(am, val.asType(EntityTag.class));
                }
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
                if (mechanism.requireObject(EntityTag.class)) {
                    am.getImmunityTable().setCooldown(BukkitAdapter.adapt(val.asType(EntityTag.class).getBukkitEntity()));
                }
                break;
        }
    }
}
