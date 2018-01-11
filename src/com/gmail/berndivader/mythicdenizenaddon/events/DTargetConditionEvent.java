package com.gmail.berndivader.mythicdenizenaddon.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;

public class DTargetConditionEvent extends SkillCondition 
implements
IEntityComparisonCondition,
ILocationComparisonCondition {

	private String ConditionName;
	private String ConditionArgs;
	private boolean bool;

	public DTargetConditionEvent(String line, MythicLineConfig mlc) {
		super(line);
		this.ACTION = ConditionAction.REQUIRED;
		this.ConditionName = mlc.getString(new String[]{"condition","c"},"");
		this.ConditionArgs = mlc.getString(new String[]{"args", "a"},"");
		this.bool = true;
	}

	@Override
	public boolean check(AbstractLocation c, AbstractLocation t) {
		Location caster = BukkitAdapter.adapt(c);
		Location target = BukkitAdapter.adapt(t);
		MMDenizenTargetConditionEvent e = new MMDenizenTargetConditionEvent(null, null, caster, target, this.ConditionName, this.ConditionArgs, this.bool);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.getBool();
	}

	@Override
	public boolean check(AbstractEntity c, AbstractEntity t) {
		Entity caster = BukkitAdapter.adapt(c);
		Entity target = BukkitAdapter.adapt(t);
		MMDenizenTargetConditionEvent e = new MMDenizenTargetConditionEvent(caster, target, null, null, this.ConditionName, this.ConditionArgs, this.bool);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.getBool();
	}

}
