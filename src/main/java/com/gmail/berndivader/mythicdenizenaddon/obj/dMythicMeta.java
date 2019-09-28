package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class dMythicMeta implements ObjectTag, Adjustable {

    static Map<String, SkillMetadata> objects = new HashMap<>();
    public static String id = "mythicmeta@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicMeta valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicmeta")
    public static dMythicMeta valueOf(String name, TagContext context) {
        if (name == null) {
            return null;
        }
        if (dMythicMeta.objects.containsKey(name)) {
            SkillMetadata data = dMythicMeta.objects.get(name);
            return new dMythicMeta(data);
        }
        return null;
    }

    public static ListTag getEntityTargets(HashSet<AbstractEntity> entities) {
        ListTag list = new ListTag();
        for (AbstractEntity e : entities) {
            list.add(new EntityTag(e.getBukkitEntity()).identify());
        }
        return list;
    }

    public static ListTag getLocationTargets(HashSet<AbstractLocation> locations) {
        ListTag list = new ListTag();
        for (AbstractLocation l : locations) {
            list.add(new LocationTag(BukkitAdapter.adapt(l)).identify());
        }
        return list;
    }

    private String prefix;
    SkillMetadata meta;

    public dMythicMeta(String id) {
        this(objects.get(id));
    }

    public dMythicMeta(SkillMetadata data) {
        this.meta = data;
        if (data != null) {
            dMythicMeta.objects.put(identify(), meta);
        }
    }

    public SkillMetadata getSkillMetadata() {
        return meta;
    }

    /*
    @Override
    protected void finalize() throws Throwable {
        dMythicMeta.objects.remove(this.identify());
        super.finalize();
    }
    */

    @Override
    public String getObjectType() {
        return "MythicMeta";
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
        if (meta != null) {
            return id + meta.hashCode();
        }
        return null;
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
        return identify();
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("caster")) {
            return new EntityTag(this.meta.getCaster().getEntity().getBukkitEntity()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("cause")) {
            return new ElementTag(this.meta.getCause().toString()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("trigger")) {
            return new EntityTag(this.meta.getTrigger().getBukkitEntity()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("origin")) {
            return new LocationTag(BukkitAdapter.adapt(this.meta.getOrigin())).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("targets")) {
            ListTag targets = new ListTag();
            if (this.meta.getEntityTargets() != null && !this.meta.getEntityTargets().isEmpty()) {
                for (AbstractEntity entity : meta.getEntityTargets()) {
                    targets.addObject(new EntityTag(entity.getBukkitEntity()));
                }
            } else if (this.meta.getLocationTargets() != null && !this.meta.getLocationTargets().isEmpty()) {
                for (AbstractLocation location : meta.getLocationTargets()) {
                    targets.addObject(new LocationTag(BukkitAdapter.adapt(location)));
                }
            }
            return targets.getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("power")) {
            return new ElementTag(this.meta.getPower()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        switch (mechanism.getName().toLowerCase()) {
            case "power":
                if (mechanism.requireFloat()) {
                    meta.setPower(mechanism.getValue().asFloat());
                }
                break;
            case "origin":
                if (mechanism.requireObject(LocationTag.class)) {
                    meta.setOrigin(BukkitAdapter.adapt(mechanism.getValue().asType(LocationTag.class)));
                }
                break;
            case "cancel":
                meta.cancelEvent();
                break;
            case "caster":
            case "trigger":
                if (mechanism.requireObject(EntityTag.class)) {
                    meta.setCaster(new GenericCaster(BukkitAdapter.adapt(mechanism.getValue().asType(EntityTag.class).getBukkitEntity())));
                }
                break;
            case "targets":
                ListTag list = mechanism.getValue().asType(ListTag.class);
                HashSet<AbstractLocation> locations;
                HashSet<AbstractEntity> entities;
                AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>> pair = Utils.splitTargetList(list);
                locations = pair.getValue();
                entities = pair.getKey();

                if (!locations.isEmpty()) {
                    meta.setLocationTargets(locations);
                } else if (!entities.isEmpty()) {
                    meta.setEntityTargets(entities);
                }
        }
    }
}
