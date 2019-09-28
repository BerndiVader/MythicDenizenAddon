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
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

import java.util.Optional;

public class dMythicSkill implements ObjectTag, Adjustable {

    public static String id = "mythicskill@";

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dMythicSkill valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("mythicskill")
    public static dMythicSkill valueOf(String name, TagContext context) {
        if (name == null || name.isEmpty()) return null;
        String metaId = null;
        try {
            if (name.contains(dMythicMeta.id)) {
                String[] arr1 = name.split(dMythicMeta.id);
                name = arr1[0];
                metaId = arr1[1];
            }
            name = name.replace(id, "");
            return new dMythicSkill(name, metaId);
        } catch (Exception e) {
            Debug.echoError(e.getMessage());
        }
        return null;
    }

    private String prefix;
    Skill skill;
    public Optional<String> metaId;
    dMythicMeta data;

    public dMythicSkill(String name) {
        this(name, null);
    }

    public dMythicSkill(String name, String metaHash) {
        if (MythicMobsAddon.mythicSkillManager.getSkill(name).isPresent()) {
            this.skill = MythicMobsAddon.mythicSkillManager.getSkill(name).get();
        } else {
            Debug.echoError("MythicSkill " + name + " not present!");
        }

        this.metaId = Optional.ofNullable(metaHash);
        if (metaId.isPresent() && dMythicMeta.objects.containsKey(this.metaId.get())) {
            data = new dMythicMeta(dMythicMeta.objects.get(this.metaId.get()));
        }
    }

    public boolean isPresent() {
        return skill != null;
    }

    public boolean hasMeta() {
        return data != null && data.meta != null;
    }

    public void setSkillMetadata(SkillMetadata data) {
        if (data != null) {
            this.data.meta = data;
        }
    }

    public void setMythicMeta(dMythicMeta data) {
        this.data = data;
    }

    public dMythicMeta getMythicMeta() {
        return data;
    }

    public SkillMetadata getSkillMetadata() {
        if (data == null || data.meta == null) {
            return null;
        }
        return data.meta;
    }

    public boolean execute(SkillMetadata data) {
        if (data == null) {
            data = this.data.meta;
        }
        if (skill.isUsable(data)) {
            skill.execute(data);
            return true;
        }
        return false;
    }

    @Override
    public String getObjectType() {
        return "MythicSkill";
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
        String s1 = metaId.isPresent() ? metaId.get() : "";
        return skill != null ? id + skill.getInternalName() + s1 : null;
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

        if (attribute.startsWith("present") || attribute.startsWith("ispresent")) {
            return new ElementTag(isPresent()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("type")) {
            return new ElementTag(isPresent() ? skill.getInternalName() : null).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("data")) {
            if (data != null) {
                return data.getAttribute(attribute.fulfill(1));
            }
            return null;
        } else if (attribute.startsWith("isuseable") || attribute.startsWith("checkconditions")) {
            SkillMetadata data = null;
            if (attribute.hasContext(1) && dMythicMeta.matches(attribute.getContext(1))) {
                data = ((dMythicMeta) attribute.getContextObject(1)).meta;
            }
            else if (this.data != null) {
                data = this.data.meta;
            }
            return new ElementTag(skill.isUsable(data)).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("execute")) {
            SkillMetadata data = null;
            if (attribute.hasContext(1) && dMythicMeta.matches(attribute.getContext(1))) {
                data = ((dMythicMeta) attribute.getContextObject(1)).meta;
            }
            else if (this.data != null) {
                data = this.data.meta;
            }
            boolean isUsable = skill.isUsable(data);
            if (isUsable) {
                skill.execute(data);
            }
            return new ElementTag(isUsable).getAttribute(attribute.fulfill(1));
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
                if (element.matchesType(dMythicMeta.class)) this.data = element.asType(dMythicMeta.class);
                break;
        }
    }
}
