package com.gmail.berndivader.mythicdenizenaddon.events;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MMDenizenCustomSkill extends Event {

    private enum TargetType {
        NONE,
        LOCATION,
        ENTITY,
    }

    private static final HandlerList handlers = new HandlerList();

    private Entity targetEntity = null;
    private Entity trigger = null;
    private Location targetLocation = null;
    private SkillCaster caster;
    private String skill;
    private String args;
    private TargetType targettype;
    SkillMetadata data;

    public MMDenizenCustomSkill(SkillCaster caster, Entity target, Location targetloc, AbstractEntity t, String s, String a, SkillMetadata data) {
        this.caster = caster;
        this.skill = s;
        if (a.length() > 1 && (a.startsWith("\"") && a.endsWith("\""))) {
            this.args = a.substring(1, a.length() - 1);
        } else {
            this.args = a;
        }

        if (t != null) {
            trigger = t.getBukkitEntity();
        }

        this.targettype = TargetType.NONE;
        if (target != null) {
            this.targetEntity = target;
            this.targettype = TargetType.ENTITY;
        } else if (targetloc != null) {
            this.targetLocation = targetloc;
            this.targettype = TargetType.LOCATION;
        }

        this.data = data;
    }

    public Entity getCaster() {
        return caster.getEntity().getBukkitEntity();
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public Entity getTrigger() {
        return trigger;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public String getSkill() {
        return skill;
    }

    public String getArgs() {
        return args;
    }

    public String getTargetType() {
        return targettype.toString();
    }

    public SkillMetadata getMetadata() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
