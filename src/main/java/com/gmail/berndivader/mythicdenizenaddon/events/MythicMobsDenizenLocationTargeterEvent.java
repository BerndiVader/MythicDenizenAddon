package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashSet;

public class MythicMobsDenizenLocationTargeterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private SkillMetadata data;
    private String targeter;
    private String[] args;
    private HashSet<AbstractLocation> targets;

    public MythicMobsDenizenLocationTargeterEvent(SkillMetadata data, String targeter, String[] args) {
        this.data = data;
        this.targeter = targeter;
        this.args = args;
        this.targets = new HashSet<>();
    }

    public SkillMetadata getSkillMetadata() {
        return data;
    }

    public Entity getCaster() {
        return data.getCaster().getEntity().getBukkitEntity();
    }

    public Entity getTrigger() {
        return data.getTrigger().getBukkitEntity();
    }

    public Location getOrigin() {
        return BukkitAdapter.adapt(data.getOrigin());
    }

    public String getTargeterName() {
        return targeter;
    }

    public String[] getArgs() {
        return args;
    }

    public HashSet<AbstractLocation> getTargets() {
        return targets != null ? targets : new HashSet<>();
    }

    public String getCause() {
        return data.getCause().name();
    }

    public void addSingleTarget(Location target) {
        targets.add(BukkitAdapter.adapt(target));
    }

    public void addTargetList(ListTag dlist) {
        for (LocationTag location : dlist.filter(LocationTag.class)) {
            targets.add(BukkitAdapter.adapt(location));
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
