package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class TriggerSkill extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("activemob") && arg.matchesPrefix("activemob") && arg.matchesArgumentType(dActiveMob.class))  {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject("trigger") && arg.matchesPrefix("trigger")) {
				entry.addObject(arg.getPrefix().getValue(), arg.asElement());
			} else if (!entry.hasObject("entity") && arg.matchesPrefix("entity") && arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dEntity.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		if (!entry.hasObject("activemob") || !entry.hasObject("entity") || !entry.hasObject("trigger")) return;
		SkillTrigger trigger = SkillTrigger.API;
		try {
			trigger = SkillTrigger.valueOf(entry.getElement("trigger").asString().toUpperCase());
		} catch (Exception ex) {
			return;
		}
		ActiveMob am = ((dActiveMob)entry.getObject("activemob")).am;
		AbstractEntity ae = BukkitAdapter.adapt(((dEntity)entry.getObject("entity")).getBukkitEntity());
		@SuppressWarnings("unused")
		TriggeredSkill ts = new TriggeredSkill(trigger, am, ae, true);
	}
}
