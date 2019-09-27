package com.gmail.berndivader.mythicdenizenaddon.events;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MMDenizenTargetConditionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Entity entity;
    private Entity target;
    private Location location;
    private Location targetlocation;
    private String conditionName;
    private String condtionArgs;
    private Boolean bool;

    public MMDenizenTargetConditionEvent(Entity e, Entity t, Location l, Location tl, String n, String a, Boolean b) {
        this.entity = e;
        this.target = t;
        this.location = l;
        this.targetlocation = tl;
        this.conditionName = n;
        this.condtionArgs = a;
        this.bool = b;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getTarget() {
        return target;
    }

    public ActiveMob getActiveMob() {
        if (MythicMobsAddon.mythicMobManager.isActiveMob(entity.getUniqueId())) {
            return MythicMobsAddon.mythicApiHelper.getMythicMobInstance(entity);
        }
        return null;
    }

    public Location getLocation() {
        return location;
    }

    public Location getTargetLocation() {
        return targetlocation;
    }

    public String getName() {
        return conditionName;
    }

    public String getArgs() {
        return condtionArgs;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean b) {
        bool = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
