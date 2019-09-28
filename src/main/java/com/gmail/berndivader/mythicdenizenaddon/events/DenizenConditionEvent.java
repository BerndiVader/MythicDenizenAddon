package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DenizenConditionEvent extends BukkitScriptEvent implements Listener {

    public static DenizenConditionEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        String s1 = s.toLowerCase();
        return s1.startsWith("mm denizen condition") || s1.startsWith("mythicmobs condition");
    }

    @Override
    public boolean matches(ScriptContainer container, String a) {
        return true;
    }

    private ElementTag meet, args, type, condition;
    private EntityTag dentity;
    private LocationTag dlocation;
    public MMDenizenConditionEvent event;

    public DenizenConditionEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "MythicMobConditionEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MMDenizenConditionEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        if (tag instanceof ElementTag && ((ElementTag) tag).isBoolean()) {
            this.meet = (ElementTag) tag;
            return true;
        }
        return super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "meet":
                return meet;
            case "entity":
                return dentity;
            case "condition":
                return condition;
            case "args":
                return args;
            case "location":
                return dlocation;
            case "type":
                return type;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobConditionEvent(MMDenizenConditionEvent event) {
        this.event = event;
        this.condition = new ElementTag(event.getName());
        this.args = new ElementTag(event.getArgs());
        this.meet = new ElementTag(event.getBool());
        this.dentity = event.getEntity() != null ? new EntityTag(event.getEntity()) : null;
        this.dlocation = event.getLocation() != null ? new LocationTag(event.getLocation()) : null;
        this.type = event.getEntity() != null ? new ElementTag("e") : event.getLocation() != null ? new ElementTag("l") : new ElementTag("n");
        fire();
        event.setBool(meet.asBoolean());
    }
}
