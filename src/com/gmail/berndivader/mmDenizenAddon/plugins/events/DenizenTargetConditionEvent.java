package com.gmail.berndivader.mmDenizenAddon.plugins.events;

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
	public mmDenizenTargetConditionEvent event;
	
	private Element meet, args, type, condition;
	private dEntity dentity=null, dtarget=null;
	private dLocation dlocation=null, dlocationtarget=null;
	
	public DenizenTargetConditionEvent() {
		instance = this;
	}
	
	
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen targetcondition");
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
    	mmDenizenTargetConditionEvent.getHandlerList().unregister(this);
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
        if (name.equals("meet")) {
            return this.meet;
        } else if (name.equals("entity")) {
        	return this.dentity;
        } else if (name.equals("targetentity")) {
        	return this.dtarget;
        } else if (name.equals("condition")) {
        	return this.condition;
        } else if (name.equals("args")) {
        	return this.args;
        } else if (name.equals("location")) {
        	return this.dlocation;
        } else if (name.equals("targetlocation")) {
        	return this.dlocationtarget;
        } else if (name.equals("type")) {
        	return this.type;
        }
        return super.getContext(name);
    }
	
    @EventHandler
    public void onMythicMobConditionEvent(mmDenizenTargetConditionEvent e) {
    	this.event = e;
    	this.condition = new Element(e.getName());
    	this.args = new Element(e.getArgs());
    	this.meet = new Element(e.getBool());
   		if (e.getEntity()!=null) {
   			this.dentity = new dEntity(e.getEntity());
   	    	this.dtarget = new dEntity(e.getTarget());
   			this.type = new Element("e");
   		} else if (e.getLocation()!=null) {
   	    	this.dlocationtarget = new dLocation(e.getTargetLocation());
   	   		this.dlocation = new dLocation(e.getLocation());
   	   		this.type = new Element("l");
   		}
        fire();
        e.setBool(this.meet.asBoolean());
    }
 }
