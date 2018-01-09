package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import java.util.HashSet;

import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.dList;

public class mmDenizenEntityTargeterEvent
extends
AbstractEvent {
	private SkillMetadata data;
	private String targeter;
	private String[] args;
	private HashSet<AbstractEntity>targets;
	
	public mmDenizenEntityTargeterEvent(SkillMetadata data,String targeter, String[]args) {
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
	public String getTargeterName() {
		return this.targeter;
	}
	public String[] getArgs() {
		return this.args;
	}
	public HashSet<AbstractEntity>getTargets(){
		return this.targets;
	}
	public void addSingleTarget(Entity target) {
		this.targets.add(BukkitAdapter.adapt(target));
	}
	public void addTargetList(dList targets) {
		targets.filter(dEntity.class).stream().forEach(e->{
			this.targets.add(BukkitAdapter.adapt(e.getBukkitEntity()));
		});
	}
}
