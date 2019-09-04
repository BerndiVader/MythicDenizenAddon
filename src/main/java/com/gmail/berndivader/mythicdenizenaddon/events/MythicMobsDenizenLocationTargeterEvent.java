package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ListTag;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class 
MythicMobsDenizenLocationTargeterEvent
extends
Event 
{
	private static final HandlerList handlers = new HandlerList();
	private SkillMetadata data;
	private String targeter;
	private String[] args;
	private HashSet<AbstractLocation>targets;
	
	public MythicMobsDenizenLocationTargeterEvent(SkillMetadata data,String targeter, String[]args) {
		this.data=data;
		this.targeter=targeter;
		this.args=args;
		this.targets=new HashSet<>();
	}
	
	public SkillMetadata getSkillMetadata() {
		return this.data;
	}
	
	public Entity getCaster() {
		return this.data.getCaster().getEntity().getBukkitEntity();
	}
	public Entity getTrigger() {
		return this.data.getTrigger().getBukkitEntity();
	}
	public Location getOrigin() {
		return BukkitAdapter.adapt(this.data.getOrigin());
	}
	public String getTargeterName() {
		return this.targeter;
	}
	public String[] getArgs() {
		return this.args;
	}
	public HashSet<AbstractLocation>getTargets(){
		return this.targets!=null?this.targets:new HashSet<>();
	}
	public String getCause() {
		return this.data.getCause().name();
	}
	public void addSingleTarget(Location target) {
		this.targets.add(BukkitAdapter.adapt(target));
	}
	public void addTargetList(ListTag dlist) {
		List<LocationTag>list=dlist.filter(LocationTag.class);
		int size=list.size();
		for(int i1=0;i1<size;i1++) {
			targets.add(BukkitAdapter.adapt(list.get(i1)));
		}
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
