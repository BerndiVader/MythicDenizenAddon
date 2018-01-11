package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;

public class DenizenMythicMobDeathEvent 
extends 
BukkitScriptEvent 
implements 
Listener {
	public static DenizenMythicMobDeathEvent instance;
	public MythicMobDeathEvent e;
	
	public DenizenMythicMobDeathEvent() {
		instance=this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=s.toLowerCase();
		return s1.startsWith("mm denizen death")||s1.startsWith("mythicmobs death");
	}
	
	@Override
	public boolean matches(ScriptContainer container, String s) {
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
    public ScriptEntryData getScriptEntryData() {
    	dEntity killer=new dEntity(e.getEntity());
    	return new BukkitScriptEntryData(killer.isPlayer()?killer.getDenizenPlayer():null,killer.isNPC()?killer.getDenizenNPC():null);
    }

	@Override
    public boolean applyDetermination(ScriptContainer container,String determination) {
		String[]c=determination.toLowerCase().split(";");
		for (int a=0;a<c.length;a++) {
			String[]parse=c[a].split(":");
			String d=parse[0].toLowerCase();
			String v=parse[1];
			switch(d) {
			case "drops":
				if (aH.Argument.valueOf(v).matchesArgumentType(dList.class)) {
					List<ItemStack>is=new ArrayList<ItemStack>();
					for(dItem di:aH.Argument.valueOf(v).asType(dList.class).filter(dItem.class)) {
						is.add(di.getItemStack());
					}
					e.setDrops(is);
				}
				break;
			case "money":
				if (aH.Argument.valueOf(v).matchesPrimitive(PrimitiveType.Double)) e.setCurrency(Double.parseDouble(v));
				break;
			case "exp":
			case "xp":
				if (aH.Argument.valueOf(v).matchesPrimitive(PrimitiveType.Integer)) e.setExp(Integer.parseInt(v));
				break;
			}
		}
		return true;
    }
	
	@Override
    public dObject getContext(String name) {
		switch(name.toLowerCase()) {
		case "drops":
			dList dl=new dList();
			for (ItemStack i:e.getDrops()) {
				dl.add(new dItem(i).identify());
			}
			return dl;
		case "killer":
			return new dEntity(e.getKiller());
		case "victim":
			return new dEntity(e.getEntity());
		case "activemob":
			return new dActiveMob(e.getMob());
		case "money":
			return new Element(e.getCurrency());
		case "exp":
			return new Element(e.getExp());
		case "event":
			return new Element(this.e.toString());
		}
        return super.getContext(name);
    }
	
	@EventHandler
	public void onMythicMobsDeathEvent(MythicMobDeathEvent e) {
		this.e=e;
		fire();
	}
}
