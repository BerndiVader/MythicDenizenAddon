package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DenizenSkillEvent extends BukkitScriptEvent implements Listener {

    public static DenizenSkillEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        String s1 = CoreUtilities.toLowerCase(s);
        return s1.startsWith("mm denizen mechanic") || s1.startsWith("mythicmobs skill");
    }

    @Override
    public boolean matches(ScriptContainer container, String a) {
        return true;
    }

    public MMDenizenCustomSkill event;
    private ElementTag skill, args, targetType;
    private EntityTag caster, target, trigger;
    private LocationTag targetLoc;
    dMythicMeta data;

    public DenizenSkillEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "MythicMobCustomMechanic";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MMDenizenCustomSkill.getHandlerList().unregister(this);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "skill":
                return skill;
            case "args":
                return args;
            case "caster":
            case "entity":
                return caster;
            case "target":
                return target;
            case "targetlocation":
                return targetLoc;
            case "trigger":
                return trigger;
            case "targettype":
                return targetType;
            case "data":
                return data;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicDenizenSkillEvent(MMDenizenCustomSkill event) {
        this.event = event;
        this.skill = new ElementTag(event.getSkill());
        this.args = new ElementTag(event.getArgs());
        this.caster = event.getCaster() != null ? new EntityTag(event.getCaster()) : null;
        this.target = event.getTargetEntity() != null ? new EntityTag(event.getTargetEntity()) : null;
        this.targetLoc = event.getTargetLocation() != null ? new LocationTag(event.getTargetLocation()) : null;
        this.targetType = new ElementTag(event.getTargetType());
        this.trigger = event.getTrigger() != null ? new EntityTag(event.getTrigger()) : null;
        this.data = new dMythicMeta(event.getMetadata());
        fire();
    }

}
