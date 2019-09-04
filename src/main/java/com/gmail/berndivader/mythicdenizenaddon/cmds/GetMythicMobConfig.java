package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public
class
GetMythicMobConfig
extends
AbstractCommand 
{
	
	static MobManager mobmanager;
	static MythicMobs mythicmobs;
	
	static {
		mythicmobs=MythicMobs.inst();
		mobmanager=mythicmobs.getMobManager();
	}
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if(!entry.hasObject(Statics.str_filter)&&arg.matchesPrefix(Statics.str_filter)) {
				entry.addObject(Statics.str_filter,arg.asElement());
			} else if(!entry.hasObject(Statics.str_strict)&&arg.matchesPrefix(Statics.str_strict)) {
				entry.addObject(Statics.str_strict,arg.asElement());
			}
		}
		if (!entry.hasObject(Statics.str_filter)) entry.addObject(Statics.str_filter,new ElementTag(new String("")));
		if (!entry.hasObject(Statics.str_strict)) entry.addObject(Statics.str_strict,new ElementTag(false));
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Pattern p=Pattern.compile(entry.getElement(Statics.str_filter).asString());
		if (!entry.getElement(Statics.str_strict).asBoolean()) {
			Iterator<String>it=mobmanager.getMobNames().iterator();
			ListTag list=new ListTag();
			while(it.hasNext()) {
				String s1=it.next();
				if (p.matcher(s1).find()) list.addObject(new dMythicMob(s1));
			}
			entry.addObject("mythicmobs",list);
		} else {
			dMythicMob mm=new dMythicMob(p.pattern());
			if(mm.isPresent()) {
				entry.addObject("mythicmob",mm);
			} else {
				throw new CommandExecutionException("Failed to create "+dMythicMob.class.getSimpleName());
			}
		}
	}
}
