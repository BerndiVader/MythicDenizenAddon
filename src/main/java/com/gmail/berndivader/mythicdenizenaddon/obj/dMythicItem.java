package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;

public class dMythicItem implements ObjectTag {

    public static String id = "mythicitem@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicItem valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicitem")
    public static dMythicItem valueOf(String name, TagContext context) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        try {
            return new dMythicItem(name.replace(id, ""));
        } catch (Exception e) {
            Debug.echoError(e.getMessage());
        }

        return null;
    }

    private String prefix;
    MythicItem mythicItem;

    public dMythicItem(String name) {
        this.mythicItem = MythicMobsAddon.mythicItemManager.getItem(name).orElse(null);
    }

    public boolean isPresent() {
        return this.mythicItem != null;
    }

    private ItemTag getItemstack() {
        return new ItemTag(BukkitAdapter.adapt(mythicItem.generateItemStack(1)));
    }

    @Override
    public String getObjectType() {
        return "MythicItem";
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
        return this.mythicItem != null ? id + this.mythicItem.getInternalName() : null;
    }

    @Override
    public String identifySimple() {
        return identify();
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public String toString() {
        return this.identify();
    }

    @Override
    public String getAttribute(Attribute a) {
        if (a == null) {
            return null;
        }

        if (a.startsWith("internalname") || a.startsWith("type")) {
            return new ElementTag(mythicItem.getInternalName()).getAttribute(a.fulfill(1));
        } else if (a.startsWith("amount")) {
            return new ElementTag(mythicItem.getAmount()).getAttribute(a.fulfill(1));
        } else if (a.startsWith("displayname")) {
            return new ElementTag(mythicItem.getDisplayName()).getAttribute(a.fulfill(1));
        } else if (a.startsWith("lore")) {
            ListTag dl = new ListTag();
            dl.addAll(mythicItem.getLore());
            return dl.getAttribute(a.fulfill(1));
        } else if (a.startsWith("materialname")) {
            return new ElementTag(mythicItem.getMaterialName()).getAttribute(a.fulfill(1));
        } else if (a.startsWith("materialdata")) {
            return new ElementTag(mythicItem.getMaterialData()).getAttribute(a.fulfill(1));
        } else if (a.startsWith("itemstack") || a.startsWith("get_item")) {
            return getItemstack().getAttribute(a.fulfill(1));
        } else if (a.startsWith("ispresent")) {
            return new ElementTag(isPresent()).getAttribute(a.fulfill(1));
        }

        return null;
    }
}
