package com.gmail.berndivader.mythicdenizenaddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicdenizenaddon.MythicDenizenPlugin;
import com.gmail.berndivader.mythicdenizenaddon.drops.DenizenScriptDrop;
import com.gmail.berndivader.mythicdenizenaddon.events.DConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DEntityTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DLocationTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DMechanicEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DSpawnConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DTargetConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.mechanics.DenizenScriptMechanic;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
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
		switch(e.getMechanicName().toLowerCase()) {
			case "dskill":
				e.register(new DMechanicEvent(e.getContainer().getConfigLine(),e.getConfig()));
				break;
			case "dscript":
				e.register(new DenizenScriptMechanic(e.getContainer().getConfigLine(),e.getConfig()));
				break;
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
	
	@EventHandler
	public void mythicMobsCustomDrop(MythicDropLoadEvent e) {
		if(e.getDropName().toLowerCase().equals("dscriptdrop")||e.getDropName().toLowerCase().equals("dscript")) {
			e.register(new DenizenScriptDrop(e.getContainer().getLine(),e.getConfig(),e.getContainer().getAmount()));
		}
	}
}
