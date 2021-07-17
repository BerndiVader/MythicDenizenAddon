package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class DMechanicEvent extends SkillMechanic implements ITargetedEntitySkill, 
ITargetedLocationSkill,
INoTargetSkill {
	
	private String skill, args;

	public DMechanicEvent(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		this.threadSafetyLevel = ThreadSafetyLevel.EITHER;
		this.skill = mlc.getString(new String[]{"skill", "s"}, "");
		this.args = mlc.getString(new String[]{"args", "arg", "a"},"");
	}

	@Override
	public boolean cast(SkillMetadata data) {
		SkillCaster caster = data.getCaster();
		String a = SkillString.parseMobVariables(this.args, caster, caster.getEntity(), data.getTrigger());
		MMDenizenCustomSkill e = new MMDenizenCustomSkill(caster,null,null,data.getTrigger(),skill,a,data);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation target) {
		SkillCaster caster = data.getCaster();
		String a = SkillString.parseMobVariables(this.args, caster, caster.getEntity(), data.getTrigger());
		MMDenizenCustomSkill e = new MMDenizenCustomSkill(caster,null,BukkitAdapter.adapt(target),data.getTrigger(),skill,a,data);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		SkillCaster caster = data.getCaster();
		String a = SkillString.parseMobVariables(this.args, caster, target, data.getTrigger());
		MMDenizenCustomSkill e = new MMDenizenCustomSkill(caster,BukkitAdapter.adapt(target),null,data.getTrigger(),skill,a,data);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}
}
