package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

public 
class
DenizenSkillEvent
extends
BukkitScriptEvent 
implements
Listener
{
	
	public static DenizenSkillEvent instance;
	public MMDenizenCustomSkill event;
	
	private ElementTag skill, args, targetType;
	private EntityTag caster, target, trigger;
	private LocationTag targetLoc;
	dMythicMeta data;
	
	public DenizenSkillEvent() {
		instance = this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=CoreUtilities.toLowerCase(s);
		return s1.startsWith("mm denizen mechanic")||s1.startsWith("mythicmobs skill");
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
		Bukkit.getServer().getPluginManager().registerEvents(this,DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MMDenizenCustomSkill.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptPath container, ObjectTag d) {
		return Boolean.parseBoolean(d.toString());
    }
	
	@Override
    public ObjectTag getContext(String name) {
		switch(name.toLowerCase()) {
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
    public void onMythicDenizenSkillEvent(MMDenizenCustomSkill e) {
    	this.skill=new ElementTag(e.getSkill());
    	this.args=new ElementTag(e.getArgs());
    	this.caster=e.getCaster()!=null?new EntityTag(e.getCaster()):null;
    	this.target=e.getTargetEntity()!=null?new EntityTag(e.getTargetEntity()):null;
    	this.targetLoc=e.getTargetLocation()!=null?new LocationTag(e.getTargetLocation()):null;
    	this.targetType=new ElementTag(e.getTargetType());
    	this.trigger=e.getTrigger()!=null?new EntityTag(e.getTrigger()):null;
    	this.data=new dMythicMeta(e.getMetadata());
    	this.event=e;
        fire();
    }

}
