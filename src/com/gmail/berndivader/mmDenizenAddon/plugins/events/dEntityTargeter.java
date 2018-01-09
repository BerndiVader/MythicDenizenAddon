package com.gmail.berndivader.mmDenizenAddon.plugins.events;

import java.util.HashSet;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

public class dEntityTargeter
extends
IEntitySelector {
	private String targeter;
	private String[] args;

	public dEntityTargeter(MythicLineConfig mlc) {
		super(mlc);
		this.targeter=mlc.getString(new String[] {"targeter","name","tpye","t","n"},"");
		this.args=mlc.getString(new String[] {"args","a"},"").split(",");
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		mmDenizenEntityTargeterEvent e=new mmDenizenEntityTargeterEvent(data,this.targeter,this.args);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.getTargets();
	}
}
