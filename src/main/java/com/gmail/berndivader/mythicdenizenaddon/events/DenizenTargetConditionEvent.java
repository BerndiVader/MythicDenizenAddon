package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class DenizenTargetConditionEvent extends BukkitScriptEvent implements Listener {

	public static DenizenTargetConditionEvent instance;
	public MMDenizenTargetConditionEvent event;
	
	private Element meet, args, type, condition;
	private dEntity dentity=null, dtarget=null;
	private dLocation dlocation=null, dlocationtarget=null;
	
	public DenizenTargetConditionEvent() {
		instance = this;
	}
	
	
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=CoreUtilities.toLowerCase(s);
		return s1.startsWith("mm denizen targetcondition")||s1.startsWith("mythicmobs targetcondition");
	}
	@Override
	public boolean matches(ScriptContainer container, String a) {
		return true;
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
    public boolean applyDetermination(ScriptContainer container, String d) {
		if (aH.Argument.valueOf(d).matchesPrimitive(aH.PrimitiveType.Boolean)) {
			this.meet = new Element(d);
			return true;
		}
        return super.applyDetermination(container, d);
    }
	
	@Override
    public dObject getContext(String name) {
		switch(name.toLowerCase()) {
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
    public void onMythicMobConditionEvent(MMDenizenTargetConditionEvent e) {
    	this.event=e;
    	this.condition=new Element(e.getName());
    	this.args=new Element(e.getArgs());
    	this.meet=new Element(e.getBool());
    	this.type=e.getEntity()!=null?new Element("e"):e.getLocation()!=null?new Element("l"):null;
		this.dentity=e.getEntity()!=null?new dEntity(e.getEntity()):null;
    	this.dtarget=e.getTarget()!=null?new dEntity(e.getTarget()):null;
    	this.dlocationtarget=e.getTargetLocation()!=null?new dLocation(e.getTargetLocation()):null;
   		this.dlocation=e.getLocation()!=null?new dLocation(e.getLocation()):null;
        fire();
        e.setBool(this.meet.asBoolean());
    }
 }
