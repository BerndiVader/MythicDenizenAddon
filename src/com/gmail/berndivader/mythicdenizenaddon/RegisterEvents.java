package com.gmail.berndivader.mythicdenizenaddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.events.DConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DEntityTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DLocationTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DMechanicEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DSpawnConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DTargetConditionEvent;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;

public class RegisterEvents implements Listener {
	
	public RegisterEvents() {
		MythicDenizenPlugin.inst().getServer().getPluginManager().registerEvents(this, MythicDenizenPlugin.inst());
	}
	
	@EventHandler
	public void onMythicMobsCustomConditionsLoad(MythicConditionLoadEvent e) {
		switch(e.getConditionName().toLowerCase()) {
		case "dcondition":
			e.register(new DConditionEvent(e.getConditionName(), e.getConfig()));
			break;
		case "dtargetcondition":
			e.register(new DTargetConditionEvent(e.getConditionName(), e.getConfig()));
			break;
		case "dspawncondition":
			e.register(new DSpawnConditionEvent(e.getConditionName(), e.getConfig()));
			break;
		}
	}
	
	@EventHandler
	public void onMythicMobsCustomMechanicLoad(MythicMechanicLoadEvent e) {
		if (e.getMechanicName().toLowerCase().equals("dskill")) {
			e.register(new DMechanicEvent(e.getContainer().getConfigLine(),e.getConfig()));
		}
	}
	
	@EventHandler
	public void onMythicMobsTargeterLoad(MythicTargeterLoadEvent e) {
		switch(e.getTargeterName().toLowerCase()) {
		case "dentity":
			e.register(new DEntityTargeter(e.getConfig()));
			break;
		case "dlocation":
			e.register(new DLocationTargeter(e.getConfig()));
			break;
		}
	}
}
