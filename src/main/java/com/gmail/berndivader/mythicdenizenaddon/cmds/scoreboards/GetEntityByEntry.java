package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class GetEntityByEntry extends AbstractCommand {
	private static String str_entry="entry";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(str_entry) && arg.matchesPrefix(str_entry)) {
				entry.addObject(str_entry, arg.asElement());
			} else arg.reportUnhandled();
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String e = entry.getElement(str_entry).asString();
		Entity entity = ScoreBoardsAddon.getEntityByEntry(e);
		if (entity!=null) {
			entry.addObject(str_entry, new dEntity(entity));
		} else {
			entry.addObject(str_entry, new Element(false));
		}
	}
}
