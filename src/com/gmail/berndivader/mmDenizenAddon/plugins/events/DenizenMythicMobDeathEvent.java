package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

public class DenizenMythicMobDeathEvent extends BukkitScriptEvent implements Listener {

	public static DenizenMythicMobDeathEvent instance;
	public MythicMobDeathEvent event;
	
	private dList drops;
	private boolean dchange;
	private dEntity killer;
	private dActiveMob am;
	private Element money;
	private Element xp;
	
	public DenizenMythicMobDeathEvent() {
		instance = this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		return CoreUtilities.toLowerCase(s).startsWith("mm denizen death");
	}
	@Override
	public boolean matches(ScriptContainer container, String a) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobsDeathEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MythicMobDeathEvent.getHandlerList().unregister(this);
    }
    
	@Override
    public boolean applyDetermination(ScriptContainer container, String d) {
		if (d.startsWith("drops")) {
			d.replaceFirst("drops:", "");
			if (aH.Argument.valueOf(d).matchesArgumentType(dList.class)) {
				this.drops = aH.Argument.valueOf(d).asType(dList.class);
				this.dchange=true;
			}
		} else if (d.startsWith("money")) {
			d.replaceFirst("money:", "");
			if (aH.Argument.valueOf(d).matchesPrimitive(PrimitiveType.Double)) {
				this.money = aH.Argument.valueOf(d).asElement();
			} 
		} else if (d.startsWith("exp")) {
			d.replaceFirst("exp:", "");
			if (aH.Argument.valueOf(d).matchesPrimitive(PrimitiveType.Integer)) {
				this.xp = aH.Argument.valueOf(d).asElement();
			}
		}
		return true;
    }
	
	@Override
    public dObject getContext(String name) {
		if (name.equals("drops")) {
			return this.drops;
		} else if (name.equals("killer")) {
			return this.killer;
		} else if (name.equals("activemob")) {
			return this.am;
		} else if (name.equals("money")) {
			return this.money;
		} else if (name.equals("exp")) {
			return this.xp;
		}
        return super.getContext(name);
    }

	@EventHandler
	public void onMythicMobsDeathEvent(MythicMobDeathEvent e) {
		this.killer = new dEntity(e.getKiller());
		this.am = new dActiveMob(e.getMob());
		this.money = new Element(e.getCurrency());
		this.xp = new Element(e.getExp());
		this.drops = new dList();
		this.dchange=false;
		for (ItemStack i : e.getDrops()) {
			this.drops.add(new dItem(i).identify());
		}
		fire();
		if (this.dchange) {
			List<dItem> items = this.drops.filter(dItem.class);
			e.getDrops().clear();
			for (dItem i : items) {
				e.getDrops().add(i.getItemStack());
			}
		}
		e.setCurrency(this.money.asDouble());
		e.setExp(this.xp.asInt());
	}
}
