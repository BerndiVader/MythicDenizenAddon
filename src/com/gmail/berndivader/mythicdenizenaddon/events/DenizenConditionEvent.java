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

public class DenizenConditionEvent 
extends
BukkitScriptEvent
implements 
Listener {

	public static DenizenConditionEvent instance;
	public MMDenizenConditionEvent event;
	
	private Element meet;
	private Element args;
	private Element type;
	private Element condition;
	private dEntity dentity=null;
	private dLocation dlocation=null;
	
	public DenizenConditionEvent() {
		instance = this;
	}
	
	
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=s.toLowerCase();
		return s1.startsWith("mm denizen condition")||s1.startsWith("mythicmobs condition");
	}
	@Override
	public boolean matches(ScriptContainer container, String a) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobConditionEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MMDenizenConditionEvent.getHandlerList().unregister(this);
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
    public void onMythicMobConditionEvent(MMDenizenConditionEvent e) {
    	this.event=e;
    	this.condition=new Element(e.getName());
    	this.args=new Element(e.getArgs());
    	this.meet=new Element(e.getBool());
   		if (e.getEntity()!=null) {
   			this.dentity=new dEntity(e.getEntity());
   			this.type=new Element("e");
   		} else if (e.getLocation()!=null) {
   	   		this.dlocation=new dLocation(e.getLocation());
   	   		this.type=new Element("l");
   		}
        fire();
        e.setBool(this.meet.asBoolean());
    }
}
