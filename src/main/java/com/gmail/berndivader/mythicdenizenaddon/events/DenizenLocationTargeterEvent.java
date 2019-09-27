package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class DenizenLocationTargeterEvent extends BukkitScriptEvent implements Listener {

    public static DenizenLocationTargeterEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("mythicmobs locationtargeter");
    }

    @Override
    public boolean matches(ScriptContainer container, String s) {
        return true;
    }

    public MythicMobsDenizenLocationTargeterEvent event;
    private EntityTag caster, trigger;
    private ElementTag targeter, cause;
    private LocationTag origin;
    private dMythicMeta metaData;
    private ListTag args = new ListTag();

    public DenizenLocationTargeterEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "DenizenLocationTargeterEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MythicMobsDenizenLocationTargeterEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        if (tag instanceof ListTag) {
            event.addTargetList((ListTag) tag);
            return true;
        }
        return super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "caster":
            case "entity":
                return caster;
            case "trigger":
                return trigger;
            case "targeter":
                return targeter;
            case "args":
                return args;
            case "cause":
                return cause;
            case "orign":
                return origin;
            case "metadata":
                return metaData;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onDenizenEntityTargetEvent(MythicMobsDenizenLocationTargeterEvent event) {
        this.event = event;
        this.caster = event.getCaster() != null ? new EntityTag(event.getCaster()) : null;
        this.trigger = event.getTrigger() != null ? new EntityTag(event.getTrigger()) : null;
        this.targeter = event.getTargeterName() != null ? new ElementTag(event.getTargeterName()) : null;
        this.cause = event.getCause() != null ? new ElementTag(event.getCause()) : null;
        this.origin = event.getOrigin() != null ? new LocationTag(event.getOrigin()) : null;
        this.metaData = new dMythicMeta(event.getSkillMetadata());
        this.args.addAll(Arrays.asList(event.getArgs()));
        fire();
    }
}
