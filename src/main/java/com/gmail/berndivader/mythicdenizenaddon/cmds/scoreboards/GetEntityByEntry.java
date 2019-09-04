package com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards;

import org.bukkit.entity.Entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

public
class 
GetEntityByEntry
extends 
AbstractCommand 
{
	private static String str_entry="entry";

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
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
			entry.addObject(str_entry, new EntityTag(entity));
		} else {
			entry.addObject(str_entry, new ElementTag(false));
		}
	}
}
