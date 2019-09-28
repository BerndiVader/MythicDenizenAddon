package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

import java.util.Optional;

public class dMythicMechanic implements ObjectTag, Adjustable {

    public static String id = "mythicmechanic@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicMechanic valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicmechanic")
    public static dMythicMechanic valueOf(String name, TagContext context) {
        if (name == null || name.isEmpty()) return null;
        String metaId = null;
        try {
            if (name.contains(dMythicMeta.id)) {
                String[] arr1 = name.split(dMythicMeta.id);
                name = arr1[0];
                metaId = arr1[1];
            }
            name = name.replace(id, "");
            return new dMythicMechanic(name, metaId);
        } catch (Exception e) {
            Debug.echoError(e.getMessage());
        }
        return null;
    }

    private String prefix;
    private String name;
    SkillMechanic mechanic;
    dMythicMeta data;
    Optional<String> metaId;

    public dMythicMechanic(String name) {
        this(name, null, "");
    }

    public dMythicMechanic(String name, String line) {
        this(name, null, line);
    }

    public dMythicMechanic(String name, String metaHash, String line) {
        this.name = name;
        this.mechanic = MythicMobsAddon.mythicSkillManager.getSkillMechanic(parseLine(line));
        this.metaId = Optional.ofNullable(metaHash);

        if (mechanic == null) {
            Debug.echoError("MythicMechanic " + name + " not present!");
        }
        if (metaId.isPresent() && dMythicMeta.objects.containsKey(metaId.get())) {
            data = new dMythicMeta(dMythicMeta.objects.get(metaId.get()));
        }
    }

    public String parseLine(String line) {
        if (line == null) {
            line = "";
        }
        if (!line.startsWith("{")) {
            line = "{" + line + "}";
        }
        return MythicLineConfig.unparseBlock(name + line);
    }

    public boolean isPresent() {
        return mechanic != null;
    }

    public boolean hasMeta() {
        return data != null && data.meta != null;
    }

    public void setSkillMetadata(SkillMetadata data) {
        if (data != null) {
            this.data.meta = data;
        }
    }

    public SkillMetadata getSkillMetadata() {
        if (data != null && data.meta != null) {
            return this.data.meta;
        }
        return null;
    }


    public void setMythicMeta(dMythicMeta data) {
        this.data = data;
    }

    public dMythicMeta getMythicMeta() {
        return data;
    }

    public boolean execute(SkillMetadata data) {
        if (data == null) {
            data = this.data.meta;
        }
        return false;
    }

    public void init(String line) {
        if (mechanic != null) {
            mechanic.init(line, new MythicLineConfig(line));
        }
    }

    @Override
    public String getObjectType() {
        return "MythicMechanic";
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
        String s1 = this.metaId.orElse("");
        return this.mechanic != null ? id + this.name + s1 : null;
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

    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        int i = 1;

        if (attribute.startsWith("present") || attribute.startsWith("ispresent")) {
            return new ElementTag(isPresent()).getAttribute(attribute.fulfill(i));
        } else if (attribute.startsWith("type")) {
            return new ElementTag(isPresent() ? name : null).getAttribute(attribute.fulfill(i));
        } else if (attribute.startsWith("data")) {
            if (data != null) {
                return data.getAttribute(attribute.fulfill(i));
            }
            return null;
        } else if (attribute.startsWith("is_useable") || attribute.startsWith("isuseable")) {
            SkillMetadata skillMeta = null;
            if (data != null) {
                skillMeta = data.meta;
            }
            if (attribute.hasContext(i) && dMythicMeta.matches(attribute.getContext(i))) {
                skillMeta = ((dMythicMeta) attribute.getContextObject(i)).meta;
            }
            return new ElementTag(mechanic.usable(skillMeta)).getAttribute(attribute.fulfill(i));
        } else if (attribute.startsWith("execute")) {
            SkillMetadata skillMeta = null;
            if (data != null) {
                skillMeta = data.meta;
            }
            if (attribute.hasContext(i) && dMythicMeta.matches(attribute.getContext(i))) {
                skillMeta = ((dMythicMeta) attribute.getContextObject(i)).meta;
            }
            return new ElementTag(mechanic.execute(skillMeta)).getAttribute(attribute.fulfill(i));
        }

        return null;
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag element = mechanism.getValue();
        switch (mechanism.getName().toLowerCase()) {
            case "data":
                if (element.matchesType(dMythicMeta.class)) {
                    this.data = element.asType(dMythicMeta.class);
                }
                break;
            case "init":
                if (element.isString()) {
                    init(element.asString());
                }
                break;
        }
    }
}
