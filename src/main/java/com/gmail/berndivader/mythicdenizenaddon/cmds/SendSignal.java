package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.entity.Entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;

public
class 
SendSignal
extends
AbstractCommand
{

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_activemob) && arg.matchesPrefix(Statics.str_activemob) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Statics.str_activemob, arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_signal) && arg.matchesPrefix(Statics.str_signal)) {
				entry.addObject(Statics.str_signal, arg.asElement());
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)
					&& arg.matchesArgumentType(EntityTag.class)) {
				entry.addObject(Statics.str_trigger, arg.asType(EntityTag.class));
			}
		}
		
		if (!entry.hasObject(Statics.str_trigger)) {
			entry.addObject(Statics.str_trigger, new EntityTag(((dActiveMob)entry.getObject(Statics.str_activemob)).getEntity()));
		}
	}
	@Override
	public void execute(ScriptEntry entry) {
		ActiveMob am = ((dActiveMob)entry.getObject(Statics.str_activemob)).getActiveMob();
		String signal = entry.getElement(Statics.str_signal).asString();
		Entity trigger = ((EntityTag)entry.getObject(Statics.str_trigger)).getBukkitEntity();
		am.signalMob(BukkitAdapter.adapt(trigger),signal);
	}
}
