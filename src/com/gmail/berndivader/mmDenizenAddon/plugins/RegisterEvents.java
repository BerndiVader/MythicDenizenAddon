package com.gmail.berndivader.mmDenizenAddon.plugins;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmDenizenAddon.MythicDenizenPlugin;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class RegisterEvents implements Listener {
	
	public RegisterEvents() {
		MythicDenizenPlugin.inst().getServer().getPluginManager().registerEvents(this, MythicDenizenPlugin.inst());
	}
	
	@EventHandler
	public void onMythicMobsCustomConditionsLoad(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("dcondition")) {
			SkillCondition condition;
			if ((condition = new dConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(condition);
		}
	}	
}
