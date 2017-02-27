package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mmDenizenAddon.plugins.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class SendSignal extends AbstractCommand {
	private enum Types {
		activemob,
		signal,
		trigger
	}

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.activemob.name()) && arg.matchesPrefix(Types.activemob.name()) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.activemob.name(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Types.signal.name()) && arg.matchesPrefix(Types.signal.name())) {
				entry.addObject(Types.signal.name(), arg.asElement());
			} else if (!entry.hasObject(Types.trigger.name()) && arg.matchesPrefix(Types.trigger.name())
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.trigger.name(), arg.asType(dEntity.class));
			}
		}
		
		if (!entry.hasObject(Types.trigger.name())) {
			entry.addObject(Types.trigger.name(), new dEntity(((dActiveMob)entry.getObject(Types.activemob.name())).entity));
		}
	}
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am = ((dActiveMob)entry.getObject(Types.activemob.name())).am;
		String signal = entry.getElement(Types.signal.name()).asString();
		Entity trigger = ((dEntity)entry.getObject(Types.trigger.name())).getBukkitEntity();
		am.signalMob(BukkitAdapter.adapt(trigger), signal);
	}
}
