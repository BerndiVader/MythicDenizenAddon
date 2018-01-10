package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dMythicMob;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class DenizenMythicMobSpawnEvent 
extends
BukkitScriptEvent
implements
Listener {
	public static DenizenMythicMobSpawnEvent instance;
	public MythicMobSpawnEvent e;

	public DenizenMythicMobSpawnEvent() {
		instance=this;
	}
	
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen spawn")||CoreUtilities.toLowerCase(s).startsWith("mythicmobs spawn");
	}
	
	@Override
	public boolean matches(ScriptContainer container, String a) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobsSpawnEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this,DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MythicMobSpawnEvent.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptContainer container, String d) {
		if (aH.Argument.valueOf(d).matchesPrimitive(aH.PrimitiveType.Boolean)&&Boolean.parseBoolean(d)) {
			e.setCancelled();
		} else if(aH.Argument.valueOf(d).matchesPrimitive(aH.PrimitiveType.Integer)) {
			e.setMobLevel(Integer.parseInt(d));
		}
        return true;
    }
	
	@Override
    public dObject getContext(String name) {
		switch(name) {
		case "entity":
			return new dEntity(e.getEntity());
		case "level":
			return new Element(e.getMobLevel());
		case "location":
			return new dLocation(e.getLocation());
		case "mobtype":
			return new Element(e.getMobType().getInternalName());
		case "mythicmob":
			return new dMythicMob(e.getMobType());
		case "cancelled":
		case "iscancelled":
			return new Element(e.isCancelled());
		}
        return super.getContext(name);
    }
	
    @EventHandler
    public void onMythicMobsSpawnEvent(MythicMobSpawnEvent e) {
    	this.e=e;
        fire();
    }
}
