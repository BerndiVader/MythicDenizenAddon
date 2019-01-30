package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.Types;
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
			if (!entry.hasObject(Types.activemob.a()) && arg.matchesPrefix(Types.activemob.a()) 
					&& arg.matchesArgumentType(dActiveMob.class)) {
				entry.addObject(Types.activemob.a(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Types.signal.a()) && arg.matchesPrefix(Types.signal.a())) {
				entry.addObject(Types.signal.a(), arg.asElement());
			} else if (!entry.hasObject(Types.trigger.a()) && arg.matchesPrefix(Types.trigger.a())
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.trigger.a(), arg.asType(dEntity.class));
			}
		}
		
		if (!entry.hasObject(Types.trigger.a())) {
			entry.addObject(Types.trigger.a(), new dEntity(((dActiveMob)entry.getObject(Types.activemob.a())).getEntity()));
		}
	}
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		ActiveMob am = ((dActiveMob)entry.getObject(Types.activemob.a())).getActiveMob();
		String signal = entry.getElement(Types.signal.a()).asString();
		Entity trigger = ((dEntity)entry.getObject(Types.trigger.a())).getBukkitEntity();
		am.signalMob(BukkitAdapter.adapt(trigger),signal);
	}
}
