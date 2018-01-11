package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class DenizenSkillEvent extends BukkitScriptEvent implements Listener {
	
	public static DenizenSkillEvent instance;
	public MMDenizenCustomSkill event;
	
	private Element skill, args, targetType;
	private dEntity caster, target, trigger;
	private dLocation targetLoc;
	
	public DenizenSkillEvent() {
		instance = this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen mechanic");
	}
	@Override
	public boolean matches(ScriptContainer container, String a) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobCustomMechanic";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MMDenizenCustomSkill.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptContainer container, String d) {
        return super.applyDetermination(container, d);
    }
	
	@Override
    public dObject getContext(String name) {
		switch(name.toLowerCase()) {
		case "skill":
			return skill;
		case "args":
			return args;
		case "caster":
			return caster;
		case "target":
			return target;
		case "targetlocation":
			return targetLoc;
		case "trigger":
			return trigger;
		case "targettype":
			return targetType;
		}
        return super.getContext(name);
    }
	
    @EventHandler
    public void onMythicDenizenSkillEvent(MMDenizenCustomSkill e) {
    	this.skill = new Element(e.getSkill());
    	this.args = new Element(e.getArgs());
    	this.caster = new dEntity(e.getCaster());
    	if (e.getTargetEntity()!=null) this.target = new dEntity(e.getTargetEntity());
    	if (e.getTargetLocation()!=null) this.targetLoc = new dLocation(e.getTargetLocation());
    	this.targetType = new Element(e.getTargetType());
    	this.trigger = new dEntity(e.getTrigger());
    	this.event = e;
        fire();
    }

}
