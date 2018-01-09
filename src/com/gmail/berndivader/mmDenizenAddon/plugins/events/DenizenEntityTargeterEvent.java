package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
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
	public mmDenizenEntityTargeterEvent event;
	private SkillMetadata data;
	private dEntity caster;
	private dEntity trigger;
	private Element targeter;
	private boolean changed=false;
	private dList args=new dList();
	
	public DenizenEntityTargeterEvent() {
		instance=this;
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen entitytargeter");
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
    	mmDenizenEntityTargeterEvent.getHandlerList().unregister(this);
    }
    
    @Override
    public dObject getContext(String name) {
    	name=name.toLowerCase();
    	if (name.equals("caster")) {
    		return this.caster;
    	} else if (name.equals("trigger")) {
    		return this.trigger;
    	} else if (name.equals("targeter")) {
    		return this.targeter;
    	} else if (name.equals("args")) {
    		return this.args;
    	}
    	return super.getContext(name);
    }
    
    public void onDenizenEntityTargetEvent(mmDenizenEntityTargeterEvent e) {
    	this.event = e;
    	this.caster=new dEntity(e.getCaster());
    	this.trigger=new dEntity(e.getTrigger());
    	this.targeter=new Element(e.getTargeterName());
    	this.args.addAll(Arrays.asList(e.getArgs()));
    	this.changed=false;
        fire();
    }
    

}
