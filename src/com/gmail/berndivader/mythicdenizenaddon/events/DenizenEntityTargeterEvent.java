package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class DenizenEntityTargeterEvent
extends
BukkitScriptEvent
implements
Listener {
	public static DenizenEntityTargeterEvent instance;
	public MMDenizenEntityTargeterEvent event;
	private dEntity caster;
	private dEntity trigger;
	private Element targeter;
	private dList args=new dList();
	
	public DenizenEntityTargeterEvent() {
		instance=this;
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mythicmobs entitytargeter");
	}
	@Override
	public boolean matches(ScriptContainer container, String s) {
		return true;
	}
	@Override
	public String getName() {
		return "DenizenEntityTargeterEvent";
	}
    @Override
    public void destroy() {
    	MMDenizenEntityTargeterEvent.getHandlerList().unregister(this);
    }
    
    @Override
    public dObject getContext(String name) {
    	switch(name.toLowerCase()) {
    	case "caster":
    	case "entity":
    		return caster;
    	case "trigger":
    		return trigger;
    	case "targeter":
    		return targeter;
    	case "args":
    		return args;
    	}
    	return super.getContext(name);
    }
    
    public void onDenizenEntityTargetEvent(MMDenizenEntityTargeterEvent e) {
    	this.event = e;
    	this.caster=e.getCaster()!=null?new dEntity(e.getCaster()):null;
    	this.trigger=e.getTrigger()!=null?new dEntity(e.getTrigger()):null;
    	this.targeter=e.getTargeterName()!=null?new Element(e.getTargeterName()):null;
    	this.args.addAll(Arrays.asList(e.getArgs()));
        fire();
    }
    

}
