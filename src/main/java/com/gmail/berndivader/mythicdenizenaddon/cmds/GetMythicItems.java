package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicItem;

import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class GetMythicItems
extends
AbstractCommand {

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(Statics.str_filter)&&arg.matchesPrefix(Statics.str_filter)) {
				entry.addObject(Statics.str_filter,arg.asElement());
			} else if(!entry.hasObject(Statics.str_strict)&&arg.matchesPrefix(Statics.str_strict)) {
				entry.addObject(Statics.str_strict,arg.asElement());
			}
		}
		if (!entry.hasObject(Statics.str_filter)) entry.addObject(Statics.str_filter,new Element(new String("")));
		if (!entry.hasObject(Statics.str_strict)) entry.addObject(Statics.str_strict,new Element(false));
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Pattern p=Pattern.compile(entry.getElement(Statics.str_filter).asString());
		if (!entry.getElement(Statics.str_strict).asBoolean()) {
			Iterator<String>it=MythicMobsAddon.mythicmobs.getItemManager().getItemNames().iterator();
			dList list=new dList();
			while(it.hasNext()) {
				String s1=it.next();
				if (p.matcher(s1).find()) list.add(new dMythicItem(s1).identify());
			}
			entry.addObject("mythicitems",list);
		} else {
			dMythicItem mi=new dMythicItem(p.pattern());
			if(mi.isPresent()) {
				entry.addObject("mythicitem",mi);
			} else {
				throw new CommandExecutionException("Failed to create "+dMythicItem.class.getSimpleName());
			}
		}
	}
}
