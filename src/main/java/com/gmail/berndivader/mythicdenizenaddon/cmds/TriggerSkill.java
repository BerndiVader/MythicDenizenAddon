package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.gmail.berndivader.mythicdenizenaddon.AbstractTriggeredSkill;
import com.gmail.berndivader.mythicdenizenaddon.Types;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
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
			if (!entry.hasObject(Types.activemob.a()) && arg.matchesPrefix(Types.activemob.a()) && arg.matchesArgumentType(dActiveMob.class))  {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Types.trigger.a()) && arg.matchesPrefix(Types.trigger.a())) {
				entry.addObject(arg.getPrefix().getValue(), arg.asElement());
			} else if (!entry.hasObject(Types.entity.a()) && arg.matchesPrefix(Types.entity.a()) && arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dEntity.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		if (!entry.hasObject(Types.activemob.a()) || !entry.hasObject(Types.entity.a()) || !entry.hasObject(Types.trigger.a())) return;
		SkillTrigger trigger = SkillTrigger.API;
		try {
			trigger = SkillTrigger.valueOf(entry.getElement(Types.trigger.a()).asString().toUpperCase());
		} catch (Exception ex) {
			dB.log(ex.getMessage());
			return;
		}
		ActiveMob am = ((dActiveMob)entry.getObject(Types.activemob.a())).getActiveMob();
		AbstractEntity ae = BukkitAdapter.adapt(((dEntity)entry.getObject(Types.entity.a())).getBukkitEntity());
		new AbstractTriggeredSkill(trigger, am, ae);
	}
}
