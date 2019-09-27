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
import io.lumine.xikage.mythicmobs.util.types.RandomInt;

public class dMythicSpawner implements ObjectTag, Adjustable {

    static String id = "mythicspawner@";

    public dMythicSpawner(MythicSpawner mythicSpawner) {
        this.mythicSpawner = mythicSpawner;
    }

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicSpawner valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicspawner")
    public static dMythicSpawner valueOf(String uniqueName, TagContext context) {
        if (uniqueName == null) {
            return null;
        }
        try {
            uniqueName = uniqueName.replace(id, "");
            if (!MythicMobsAddon.isMythicSpawner(uniqueName)) {
                return null;
            }
            return new dMythicSpawner(MythicMobsAddon.getMythicSpawner(uniqueName));
        } catch (Exception e) {
            return null;
        }
    }

    private String prefix;
    MythicSpawner mythicSpawner;

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>'";
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
        return this.mythicSpawner != null ? id + this.mythicSpawner.getName() : null;
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

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("location")) {
            return new LocationTag(BukkitAdapter.adapt(mythicSpawner.getLocation())).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("world")) {
            return new WorldTag(BukkitAdapter.adapt(mythicSpawner.getLocation().getWorld())).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("allactivemobs")) {
            return MythicMobsAddon.getActiveMobsFromSpawner(mythicSpawner).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("mobtype")) {
            return new ElementTag(mythicSpawner.getTypeName()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("moblevel")) {
            return new ElementTag(mythicSpawner.getMobLevel().get()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("cooldown")) {
            return new ElementTag(mythicSpawner.getCooldownSeconds()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("remainingcooldown")) {
            return new ElementTag(mythicSpawner.getRemainingCooldownSeconds()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("warmup")) {
            return new ElementTag(mythicSpawner.getWarmupSeconds()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("remainingwarmup")) {
            return new ElementTag(mythicSpawner.getRemainingWarmupSeconds()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("mobamount")) {
            return new ElementTag(mythicSpawner.getNumberOfMobs()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("maxmobamount")) {
            return new ElementTag(mythicSpawner.getMaxMobs()).getAttribute(attribute.fulfill(1));
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
            case "activate":
                if (val.asBoolean()) {
                    mythicSpawner.ActivateSpawner();
                } else {
                    mythicSpawner.Disable();
                }
                break;
            case "remainingcooldown":
                mythicSpawner.setRemainingCooldownSeconds(val.asLong());
                break;
            case "cooldown":
                mythicSpawner.setCooldownSeconds(val.asInt());
                break;
            case "remainingwarmup":
                mythicSpawner.setRemainingWarmupSeconds(val.asLong());
                break;
            case "warmup":
                mythicSpawner.setWarmupSeconds(val.asInt());
                break;
            case "mobtype":
                if (MythicMobsAddon.isMythicMob(val.asString())) {
                    mythicSpawner.setType(val.asString());
                }
                break;
            case "moblevel":
                mythicSpawner.setMobLevel(new RandomInt(val.asInt()));
                break;
            case "spawn":
                mythicSpawner.Spawn();
                break;
            case "attachmob":
                MythicMobsAddon.attachMobToSpawner(mythicSpawner, val.asType(dActiveMob.class));
                break;
        }
    }
}
