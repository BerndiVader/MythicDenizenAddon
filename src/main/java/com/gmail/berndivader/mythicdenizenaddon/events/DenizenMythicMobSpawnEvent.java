package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMob;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;

public 
class
DenizenMythicMobSpawnEvent 
extends
BukkitScriptEvent
implements
Listener
{
	public static DenizenMythicMobSpawnEvent instance;
	public MythicMobSpawnEvent e;

	public DenizenMythicMobSpawnEvent() {
		instance=this;
	}
	
	@Override
	public boolean couldMatch(ScriptPath path) {
		return path.eventLower.startsWith("mm denizen spawn")||path.eventLower.startsWith("mythicmobs spawn");
	}
	
	@Override
	public boolean matches(ScriptPath path) {
		return super.matches(path);
	}

	@Override
	public String getName() {
		return "MythicMobsSpawnEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this,MythicDenizenPlugin.inst());
	}
	
    @Override
    public void destroy() {
    	MythicMobSpawnEvent.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptPath path, ObjectTag tag) {
		if(isDefaultDetermination(tag)) {
			String d=tag.toString();
			if (Argument.valueOf(d).matchesBoolean()&&Boolean.parseBoolean(d)) {
				e.setCancelled();
			} else if(Argument.valueOf(d).matchesInteger()) {
				e.setMobLevel(Integer.parseInt(d));
			}
	        return true;
		}
		return super.applyDetermination(path, tag);
    }
	
	@Override
    public ObjectTag getContext(String name) {
		switch(name.toLowerCase()) {
		case "entity":
			return new EntityTag(e.getEntity());
		case "level":
			return new ElementTag(e.getMobLevel());
		case "location":
			return new LocationTag(e.getLocation());
		case "mobtype":
			return new ElementTag(e.getMobType().getInternalName());
		case "mythicmob":
			return new dMythicMob(e.getMobType());
		case "cancelled":
		case "iscancelled":
			return new ElementTag(e.isCancelled());
		}
        return super.getContext(name);
    }
	
    @EventHandler
    public void onMythicMobsSpawnEvent(MythicMobSpawnEvent e) {
    	this.e=e;
        fire(e);
    }
}
