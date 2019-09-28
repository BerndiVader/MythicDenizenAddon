package com.gmail.berndivader.mythicdenizenaddon.events;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MMDenizenConditionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Entity entity;
    private Location location;
    private String name;
    private String args;
    private Boolean bool;

    public MMDenizenConditionEvent(Entity entity, Location location, String name, String args, boolean bool) {
        this.entity = entity;
        this.location = location;
        this.name = name;
        this.args = args;
        this.bool = bool;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public ActiveMob getActiveMob() {
        if (MythicMobsAddon.mythicMobManager.isActiveMob(entity.getUniqueId())) {
            return MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity);
        }
        return null;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public String getArgs() {
        return this.args;
    }

    public Boolean getBool() {
        return this.bool;
    }

    public void setBool(boolean b) {
        this.bool = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
