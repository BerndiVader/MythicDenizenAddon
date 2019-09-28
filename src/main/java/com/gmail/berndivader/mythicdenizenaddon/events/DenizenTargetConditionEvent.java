package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DenizenTargetConditionEvent extends BukkitScriptEvent implements Listener {

    public static DenizenTargetConditionEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        String s1 = CoreUtilities.toLowerCase(s);
        return s1.startsWith("mm denizen targetcondition") || s1.startsWith("mythicmobs targetcondition");
    }

    @Override
    public boolean matches(ScriptContainer container, String a) {
        return true;
    }

    public MMDenizenTargetConditionEvent event;
    private ElementTag meet;
    private ElementTag args;
    private ElementTag type;
    private ElementTag condition;
    private EntityTag dentity = null;
    private EntityTag dtarget = null;
    private LocationTag dlocation = null;
    private LocationTag dlocationtarget = null;

    public DenizenTargetConditionEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "MythicMobTargetConditionEvent";
    }

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MMDenizenTargetConditionEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        if (tag instanceof ElementTag && ((ElementTag) tag).isBoolean()) {
            this.meet = new ElementTag(((ElementTag) tag).asBoolean());
            return true;
        }
        return super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "meet":
                return this.meet;
            case "entity":
                return this.dentity;
            case "targetentity":
                return this.dtarget;
            case "condition":
                return this.condition;
            case "args":
                return this.args;
            case "location":
                return this.dlocation;
            case "targetlocation":
                return this.dlocationtarget;
            case "type":
                return this.type;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobConditionEvent(MMDenizenTargetConditionEvent event) {
        this.event = event;
        this.condition = new ElementTag(event.getName());
        this.args = new ElementTag(event.getArgs());
        this.meet = new ElementTag(event.getBool());
        this.type = event.getEntity() != null ? new ElementTag("e") : event.getLocation() != null ? new ElementTag("l") : null;
        this.dentity = event.getEntity() != null ? new EntityTag(event.getEntity()) : null;
        this.dtarget = event.getTarget() != null ? new EntityTag(event.getTarget()) : null;
        this.dlocationtarget = event.getTargetLocation() != null ? new LocationTag(event.getTargetLocation()) : null;
        this.dlocation = event.getLocation() != null ? new LocationTag(event.getLocation()) : null;
        fire();
        event.setBool(meet.asBoolean());
    }
}
