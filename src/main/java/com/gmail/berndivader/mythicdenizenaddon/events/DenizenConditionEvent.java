package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;

public 
class
DenizenConditionEvent 
extends
BukkitScriptEvent
implements 
Listener {

	public static DenizenConditionEvent instance;
	public MMDenizenConditionEvent event;
	
	private ElementTag meet,args,type,condition;
	private EntityTag dentity;
	private LocationTag dlocation;
	
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
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
		String d=tag.toString();
		if (Argument.valueOf(d).matchesPrimitive(PrimitiveType.Boolean)) {
			this.meet = new ElementTag(d);
		}
		return Boolean.parseBoolean(d);
    }
	
	@Override
    public ObjectTag getContext(String name) {
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
    	this.condition=new ElementTag(e.getName());
    	this.args=new ElementTag(e.getArgs());
    	this.meet=new ElementTag(e.getBool());
		this.dentity=e.getEntity()!=null?new EntityTag(e.getEntity()):null;
   		this.dlocation=e.getLocation()!=null?new LocationTag(e.getLocation()):null;
   		this.type=e.getEntity()!=null?new ElementTag("e"):e.getLocation()!=null?new ElementTag("l"):new ElementTag("n");
        fire();
        e.setBool(this.meet.asBoolean());
    }
}
