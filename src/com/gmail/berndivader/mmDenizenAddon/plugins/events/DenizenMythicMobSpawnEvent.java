package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

public class DenizenMythicMobSpawnEvent extends BukkitScriptEvent implements Listener {

	public static DenizenMythicMobSpawnEvent instance;
	public MythicMobSpawnEvent event;

	private dEntity entity;
	private Element level, mobtpye, cancel;
	private dLocation location;

	public DenizenMythicMobSpawnEvent() {
		instance = this;
	}
	
	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen spawn");
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
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MythicMobSpawnEvent.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptContainer container, String d) {
		if (aH.Argument.valueOf(d).matchesPrimitive(aH.PrimitiveType.Boolean)) {
			this.cancel = new Element(d);
			return true;
		}
        return super.applyDetermination(container, d);
    }
	
	@Override
    public dObject getContext(String name) {
		if (name.equals("entity")) {
			return this.entity;
		} else if (name.equals("level")) {
			return this.level;
		} else if (name.equals("location")) {
			return this.location;
		} else if (name.equals("mobtype")) {
			return this.mobtpye;
		} else if (name.equals("iscancelled")) {
			return this.cancel;
		}
        return super.getContext(name);
    }
	
    @EventHandler
    public void onMythicMobsSpawnEvent(MythicMobSpawnEvent e) {
    	this.entity = new dEntity(e.getEntity());
    	this.level = new Element (e.getMobLevel());
    	this.mobtpye = new Element(e.getMobType().getInternalName());
    	this.location = new dLocation(e.getLocation());
    	this.cancel = new Element(e.isCancelled());
    	this.event = e;
        fire();
        if (this.cancel.asBoolean()) e.setCancelled();
    }
}
