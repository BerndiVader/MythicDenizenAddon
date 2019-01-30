package com.gmail.berndivader.mythicdenizenaddon.events;

import java.util.HashSet;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public 
class 
DLocationTargeter
extends
ILocationSelector
{
	private String targeter;
	private String[] args;

	public DLocationTargeter(MythicLineConfig mlc) {
		super();
		this.targeter=mlc.getString(new String[] {"targeter","name","type","t","n"},"");
		this.args=mlc.getString(new String[] {"args","a"},"").split(",");
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		MythicMobsDenizenLocationTargeterEvent event=new MythicMobsDenizenLocationTargeterEvent(data,targeter,args);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event.getTargets();
	}
}
