package com.gmail.berndivader.mmDenizenAddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmDenizenAddon.MythicDenizenPlugin;
import com.gmail.berndivader.mmDenizenAddon.plugins.events.dConditionEvent;
import com.gmail.berndivader.mmDenizenAddon.plugins.events.dMechanicEvent;
import com.gmail.berndivader.mmDenizenAddon.plugins.events.dSpawnConditionEvent;
import com.gmail.berndivader.mmDenizenAddon.plugins.events.dTargetConditionEvent;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class RegisterEvents implements Listener {
	
	public RegisterEvents() {
		MythicDenizenPlugin.inst().getServer().getPluginManager().registerEvents(this, MythicDenizenPlugin.inst());
	}
	
	@EventHandler
	public void onMythicMobsCustomConditionsLoad(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("dcondition")) {
			SkillCondition c;
			if ((c = new dConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("dtargetcondition")) {
			SkillCondition c;
			if ((c = new dTargetConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("dspawncondition")) {
			SkillCondition c;
			if ((c = new dSpawnConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		}
	}
	
	@EventHandler
	public void onMythicMobsCustomMechanicLoad(MythicMechanicLoadEvent e) {
		if (e.getMechanicName().toLowerCase().equals("dskill")) {
			SkillMechanic m;
			if ((m = new dMechanicEvent(e.getContainer().getConfigLine(), e.getConfig()))!=null) e.register(m);
		}
	}
}
