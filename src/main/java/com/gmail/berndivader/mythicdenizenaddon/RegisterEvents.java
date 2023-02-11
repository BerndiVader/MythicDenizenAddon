package com.gmail.berndivader.mythicdenizenaddon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicdenizenaddon.conditions.EntityCompareScriptCondition;
import com.gmail.berndivader.mythicdenizenaddon.conditions.EntityScriptCondition;
import com.gmail.berndivader.mythicdenizenaddon.conditions.LocationCompareScriptCondition;
import com.gmail.berndivader.mythicdenizenaddon.conditions.LocationScriptCondition;
import com.gmail.berndivader.mythicdenizenaddon.drops.DenizenScriptDrop;
import com.gmail.berndivader.mythicdenizenaddon.events.DConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DEntityTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DLocationTargeter;
import com.gmail.berndivader.mythicdenizenaddon.events.DMechanicEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DSpawnConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.events.DTargetConditionEvent;
import com.gmail.berndivader.mythicdenizenaddon.mechanics.DenizenScriptMechanic;
import com.gmail.berndivader.mythicdenizenaddon.targeters.EntityTargeter;
import com.gmail.berndivader.mythicdenizenaddon.targeters.LocationTargeter;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicDropLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;

public
class
RegisterEvents 
implements 
Listener 
{
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
			case "dentity":
				e.register(new EntityScriptCondition(e.getConfig().getLine(),e.getConfig()));
				break;
			case "dentitycompare":
				e.register(new EntityCompareScriptCondition(e.getConfig().getLine(),e.getConfig()));
				break;
			case "dlocation":
				e.register(new LocationScriptCondition(e.getConfig().getLine(),e.getConfig()));
				break;
			case "dlocationcompare":
				e.register(new LocationCompareScriptCondition(e.getConfig().getLine(),e.getConfig()));
				break;
		}
	}
	
	@EventHandler
	public void onMythicMobsCustomMechanicLoad(MythicMechanicLoadEvent e) {
		switch(e.getMechanicName().toLowerCase()) {
			case "denizenskill":
			case "dskill":
				e.register(new DMechanicEvent(e.getContainer().getConfigLine(),e.getConfig()));
				break;
			case "denizenscript":
			case "dscript":
			case "denizen":
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
			case "dentityscript":
				e.register(new EntityTargeter(e.getConfig().getLine(),e.getConfig()));
				break;
			case "dlocationscript":
				e.register(new LocationTargeter(e.getConfig().getLine(),e.getConfig()));
				break;
			}
	}
	
	@EventHandler
	public void mythicMobsCustomDrop(MythicDropLoadEvent e) {
		switch(e.getDropName().toLowerCase()) {
			case "dscript":
			case "denizenscript":
			case "denizen":
				e.register(new DenizenScriptDrop(e.getContainer().getLine(),e.getConfig(),e.getContainer().getAmount()));
				break;
		}
	}
}
