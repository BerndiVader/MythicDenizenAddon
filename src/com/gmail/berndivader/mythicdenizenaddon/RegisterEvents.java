package com.gmail.berndivader.mythicdenizenaddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.events.DConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DEntityTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DMechanicEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DSpawnConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DTargetConditionEvent;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

public class RegisterEvents implements Listener {
	
	public RegisterEvents() {
		MythicDenizenPlugin.inst().getServer().getPluginManager().registerEvents(this, MythicDenizenPlugin.inst());
	}
	
	@EventHandler
	public void onMythicMobsCustomConditionsLoad(MythicConditionLoadEvent e) {
		if (e.getConditionName().toLowerCase().equals("dcondition")) {
			SkillCondition c;
			if ((c = new DConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("dtargetcondition")) {
			SkillCondition c;
			if ((c = new DTargetConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		} else if (e.getConditionName().toLowerCase().equals("dspawncondition")) {
			SkillCondition c;
			if ((c = new DSpawnConditionEvent(e.getConditionName(), e.getConfig())) != null) e.register(c);
		}
	}
	
	@EventHandler
	public void onMythicMobsCustomMechanicLoad(MythicMechanicLoadEvent e) {
		if (e.getMechanicName().toLowerCase().equals("dskill")) {
			SkillMechanic m;
			if ((m = new DMechanicEvent(e.getContainer().getConfigLine(), e.getConfig()))!=null) e.register(m);
		}
	}
	
	public void onMythicMobsTargeterLoad(MythicTargeterLoadEvent e) {
		switch(e.getTargeterName().toLowerCase()) {
		case "dentitytargeter":{
			SkillTargeter targeter=new DEntityTargeter(e.getConfig());
			e.register(targeter);
			break;
		}
//		case "dlocationtargeter":{
//			SkillTargeter targeter=new dLocationTargeter(e.getConfig());
//			e.register(targeter);
//			break;
//		}
		}
	}
}
