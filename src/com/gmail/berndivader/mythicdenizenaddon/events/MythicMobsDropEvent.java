package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitItemStack;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IIntangibleDrop;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.drops.ILocationDrop;
import io.lumine.xikage.mythicmobs.drops.IMessagingDrop;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.drops.droppables.ItemDrop;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;

public class MythicMobsDropEvent 
extends 
BukkitScriptEvent 
implements 
Listener {
	public static MythicMobsDropEvent instance;
	public MythicMobLootDropEvent e;
	private LootBag lootBag;
	
	public MythicMobsDropEvent() {
		instance=this;
	}

	@Override
	public boolean couldMatch(ScriptContainer container, String s) {
		String s1=s.toLowerCase();
		return s1.startsWith("mm lootdrop")||s1.startsWith("mythicmobs lootdrop");
	}
	
	@Override
	public boolean matches(ScriptContainer container, String s) {
		return true;
	}

	@Override
	public String getName() {
		return "MythicMobLootDropEvent";
	}

	public void init() {
		Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
	}
	
    @Override
    public void destroy() {
    	MythicMobLootDropEvent.getHandlerList().unregister(this);
    }
    
    @Override
    public ScriptEntryData getScriptEntryData() {
    	dEntity dropper=new dEntity(e.getEntity());
    	return new BukkitScriptEntryData(dropper.isPlayer()?dropper.getDenizenPlayer():null,dropper.isNPC()?dropper.getDenizenNPC():null);
    }

	@Override
    public boolean applyDetermination(ScriptContainer container,String determination) {
		if(aH.Argument.valueOf(determination).matchesArgumentType(dList.class)) {
			setDrops(lootBag,aH.Argument.valueOf(determination).asType(dList.class));
		}
		return true;
    }
	
	@Override
    public dObject getContext(String name) {
		switch(name.toLowerCase()) {
		case "drops":
			return getDrops(e,lootBag);
		case "activemob":
			return new dActiveMob(e.getMob());
		case "killer":
			return new dEntity(e.getKiller());
		}
        return super.getContext(name);
    }
	
	@EventHandler
	public void onMythicMobLootDrop(MythicMobLootDropEvent ev) {
		this.e=ev;
		this.lootBag=ev.getDrops();
		fire();
	}
	
	private static void setDrops(LootBag lootBag,dList dropList) {
		Map<Class,Drop>intangibleDrops=new HashMap<Class,Drop>();
		List<Drop>itemDrops=new ArrayList<Drop>();
		for(String type:dropList) {
			if (aH.Argument.valueOf(type).matchesArgumentType(dItem.class)) {
				BukkitItemStack bit=(BukkitItemStack)BukkitAdapter.adapt(aH.Argument.valueOf(type).asType(dItem.class).getItemStack());
				ItemDrop drop=new ItemDrop("MMDA",null,bit);
				drop.setAmount(bit.getAmount());
				itemDrops.add(drop);
			}else if (aH.Argument.valueOf(type).matchesPrimitive(PrimitiveType.String)) {
				type=type.replaceAll("/"," ");
				Drop drop=Drop.getDrop(type);
				drop.setAmount(Double.parseDouble(type.split(" ")[1]));
				intangibleDrops.put(drop.getClass(),drop);
			}
		}
		lootBag.setLootTable(itemDrops);
		lootBag.setLootTableIntangible(intangibleDrops);
	}
	
	private static dList getDrops(MythicMobLootDropEvent e,LootBag lootBag) {
		dList dropList=new dList();
		for(Drop drop:lootBag.getDrops()) {
			if(drop instanceof IItemDrop) {
				ItemStack item=BukkitAdapter.adapt(((IItemDrop) drop).getDrop(new DropMetadata(e.getMob(),BukkitAdapter.adapt(e.getKiller()))));
				dropList.addObject(new dItem(item));
				continue;
			}
			if(drop instanceof ILocationDrop||drop instanceof IIntangibleDrop||drop instanceof IMessagingDrop) {
				dropList.add(drop.getLine());
				continue;
			}
		}
		return dropList;
	}
}
