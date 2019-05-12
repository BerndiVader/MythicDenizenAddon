package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class SendSignal extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Statics.str_activemob) && arg.matchesPrefix(Statics.str_activemob) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Statics.str_activemob, arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_signal) && arg.matchesPrefix(Statics.str_signal)) {
				entry.addObject(Statics.str_signal, arg.asElement());
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Statics.str_trigger, arg.asType(dEntity.class));
			}
		}
		
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, new dEntity(((dActiveMob)entry.getObject(Statics.str_activemob)).getEntity()));
		}
	}
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am = ((dActiveMob)entry.getObject(Statics.str_activemob)).getActiveMob();
		String signal = entry.getElement(Statics.str_signal).asString();
		Entity trigger = ((dEntity)entry.getObject(Statics.str_trigger)).getBukkitEntity();
		am.signalMob(BukkitAdapter.adapt(trigger),signal);
	}
}
