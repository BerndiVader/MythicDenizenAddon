package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMob;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DenizenMythicMobSpawnEvent extends BukkitScriptEvent implements Listener {

    public static DenizenMythicMobSpawnEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("mm denizen spawn") || CoreUtilities.toLowerCase(s).startsWith("mythicmobs spawn");
    }

    @Override
    public boolean matches(ScriptContainer container, String a) {
        return true;
    }

    public MythicMobSpawnEvent event;

    public DenizenMythicMobSpawnEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "MythicMobsSpawnEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MythicMobSpawnEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        if (tag instanceof ElementTag) {
            ElementTag element = (ElementTag) tag;
            if (element.isBoolean()) {
                if (element.asBoolean()) {
                    event.setCancelled();
                }
                return true;
            }
            else if (element.isInt()) {
                event.setMobLevel(element.asInt());
                return true;
            }
        }
        return super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "entity":
                return new EntityTag(event.getEntity());
            case "level":
                return new ElementTag(event.getMobLevel());
            case "location":
                return new LocationTag(event.getLocation());
            case "mobtype":
                return new ElementTag(event.getMobType().getInternalName());
            case "mythicmob":
                return new dMythicMob(event.getMobType());
            case "cancelled":
            case "iscancelled":
                return new ElementTag(event.isCancelled());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobsSpawnEvent(MythicMobSpawnEvent event) {
        this.event = event;
        fire();
    }
}
