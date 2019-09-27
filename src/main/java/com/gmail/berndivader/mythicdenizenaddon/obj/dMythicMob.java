package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class dMythicMob implements ObjectTag {

    static String id = "mythicmob@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicMob valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicmob")
    public static dMythicMob valueOf(String uniqueName, TagContext context) {
        if (uniqueName == null) {
            return null;
        }
        try {
            uniqueName = uniqueName.replace(id, "");
            if (!MythicMobsAddon.isMythicMob(uniqueName)) {
                return null;
            }
            return new dMythicMob(MythicMobsAddon.mythicMobManager.getMythicMob(uniqueName));
        } catch (Exception e) {
            return null;
        }
    }

    String prefix;
    MythicMob mm;

    public dMythicMob(String type) {
        this.mm = MythicMobsAddon.mythicMobManager.getMythicMob(type);
    }

    public dMythicMob(MythicMob mythicMob) {
        this.mm = mythicMob;
    }

    public boolean isPresent() {
        return mm != null;
    }

    @Override
    public String getObjectType() {
        return "MythicMob";
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
    public ObjectTag setPrefix(String string) {
        this.prefix = string;
        return this;
    }

    @Override
    public String identify() {
        return this.mm != null ? id + this.mm.getInternalName() : null;
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

        if (attribute.startsWith("spawn") && attribute.hasContext(1)) {
            ObjectTag object = attribute.getContextObject(1);
            if (LocationTag.matches(object.identify())) {
                return new dActiveMob(mm.spawn(BukkitAdapter.adapt(LocationTag.valueOf(object.identify())), 1)).getAttribute(attribute.fulfill(1));
            } else if (EntityTag.matches(object.identify())) {
                return new dActiveMob(mm.spawn(BukkitAdapter.adapt(((EntityTag) object).getBukkitEntity().getLocation()), 1)).getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}
