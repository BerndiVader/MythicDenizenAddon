package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.apache.commons.lang3.tuple.Pair;

import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class TriggerSkill extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Statics.str_activemob) && arg.matchesPrefix(Statics.str_activemob) && arg.matchesArgumentType(dActiveMob.class))  {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asElement());
			} else if (!entry.hasObject(Statics.str_entity) && arg.matchesPrefix(Statics.str_entity) && arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dEntity.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		if (!entry.hasObject(Statics.str_activemob) || !entry.hasObject(Statics.str_entity) || !entry.hasObject(Statics.str_trigger)) return;
		SkillTrigger trigger = SkillTrigger.API;
		try {
			trigger = SkillTrigger.valueOf(entry.getElement(Statics.str_trigger).asString().toUpperCase());
		} catch (Exception ex) {
			dB.log(ex.getMessage());
			return;
		}
		ActiveMob am = ((dActiveMob)entry.getObject(Statics.str_activemob)).getActiveMob();
		AbstractEntity ae = BukkitAdapter.adapt(((dEntity)entry.getObject(Statics.str_entity)).getBukkitEntity());
		new TriggeredSkill(trigger,am,ae,new Pair[0]);
	}
}
