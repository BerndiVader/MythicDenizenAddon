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
import com.denizenscript.denizencore.utilities.CoreUtilities;

public 
class 
DenizenTargetConditionEvent 
extends 
BukkitScriptEvent
implements
Listener 
{

	public static DenizenTargetConditionEvent instance;
	public MMDenizenTargetConditionEvent event;
	
	private ElementTag meet, args, type, condition;
	private EntityTag dentity=null, dtarget=null;
	private LocationTag dlocation=null, dlocationtarget=null;
	
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
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
		String d=tag.toString();
		if (Argument.valueOf(d).matchesPrimitive(PrimitiveType.Boolean)) {
			this.meet = new ElementTag(d);
			return true;
		}
        return super.applyDetermination(container,tag);
    }
	
	@Override
    public ObjectTag getContext(String name) {
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
    	this.condition=new ElementTag(e.getName());
    	this.args=new ElementTag(e.getArgs());
    	this.meet=new ElementTag(e.getBool());
    	this.type=e.getEntity()!=null?new ElementTag("e"):e.getLocation()!=null?new ElementTag("l"):null;
		this.dentity=e.getEntity()!=null?new EntityTag(e.getEntity()):null;
    	this.dtarget=e.getTarget()!=null?new EntityTag(e.getTarget()):null;
    	this.dlocationtarget=e.getTargetLocation()!=null?new LocationTag(e.getTargetLocation()):null;
   		this.dlocation=e.getLocation()!=null?new LocationTag(e.getLocation()):null;
        fire();
        e.setBool(this.meet.asBoolean());
    }
 }
