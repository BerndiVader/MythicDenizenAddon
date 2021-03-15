package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

public
class
DenizenLocationTargeterEvent
extends
BukkitScriptEvent
implements
Listener 
{
	public static DenizenLocationTargeterEvent instance;
	public MythicMobsDenizenLocationTargeterEvent event;
	private EntityTag caster,trigger;
	private ElementTag targeter,cause;
	private LocationTag origin;
	private dMythicMeta metaData;
	private ListTag args=new ListTag();
	
	public DenizenLocationTargeterEvent() {
		instance=this;
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, MythicDenizenPlugin.inst());
	}
	
	@Override
	public boolean couldMatch(ScriptPath path) {
		return path.eventLower.startsWith("mythicmobs locationtargeter");
	}
	
	@Override
	public boolean matches(ScriptPath path) {
		return super.matches(path);
	}
	
	@Override
	public String getName() {
		return "DenizenLocationTargeterEvent";
	}
	
    @Override
    public void destroy() {
    	MythicMobsDenizenLocationTargeterEvent.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptPath path,ObjectTag tag) {
		if(isDefaultDetermination(tag)) {
			String determination=tag.toString();
			if(Argument.valueOf(determination).matchesArgumentType(ListTag.class)) {
				event.addTargetList((Argument.valueOf(determination).asType(ListTag.class)));
			}
			return true;
		}
		return super.applyDetermination(path, tag);
    }
    
	@Override
    public ObjectTag getContext(String name) {
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
	    	case "cause":
	    		return cause;
	    	case "orign":
	    		return origin;
	    	case "metadata":
	    		return metaData;
    	}
    	return super.getContext(name);
    }
	
	@EventHandler
    public void onDenizenEntityTargetEvent(MythicMobsDenizenLocationTargeterEvent e) {
    	this.caster=e.getCaster()!=null?new EntityTag(e.getCaster()):null;
    	this.trigger=e.getTrigger()!=null?new EntityTag(e.getTrigger()):null;
    	this.targeter=e.getTargeterName()!=null?new ElementTag(e.getTargeterName()):null;
    	this.cause=e.getCause()!=null?new ElementTag(e.getCause()):null;
    	this.origin=e.getOrigin()!=null?new LocationTag(e.getOrigin()):null;
    	this.metaData=new dMythicMeta(e.getSkillMetadata());
    	this.args.addAll(Arrays.asList(e.getArgs()));
    	this.event=e;
        fire(e);
    }
    

}
