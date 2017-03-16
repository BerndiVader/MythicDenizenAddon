package com.gmail.berndivader.mmDenizenAddon.plugins.cmds.scoreboards;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class getEntityByEntry extends AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject("entry") && arg.matchesPrefix("entry")) {
				entry.addObject("entry", arg.asElement());
			} else arg.reportUnhandled();
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		String e = entry.getElement("entry").asString();
		Entity entity = ScoreBoardsAddon.getEntityByEntry(e);
		if (entity!=null) {
			entry.addObject("entity", new dEntity(entity));
		} else {
			entry.addObject("entity", new Element(false));
		}
	}
}
