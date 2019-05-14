package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMechanic;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public 
class
GetMythicMechanic
extends
AbstractCommand 
{
	static String str_error_parse="GetMythicMechanic - argument %s is required!";
	static String str_error_execute="GetMythicMechanic - mechanic with the name %s is not present!";
	static MythicMobs mythicmobs;
	static MobManager mobmanager;
	
	static {
		mythicmobs=MythicMobs.inst();
		mobmanager=mythicmobs.getMobManager();
	}
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(Statics.str_name)&&arg.matchesPrefix(Statics.str_name)) {
				entry.addObject(Statics.str_name,arg.asElement());
			} else if(!entry.hasObject(Statics.str_line)&&arg.matchesPrefix(Statics.str_line)) {
				entry.addObject(Statics.str_line,arg.asElement());
			} else if(!entry.hasObject(Statics.str_data)&&arg.matchesPrefix(Statics.str_data)) {
				entry.addObject(Statics.str_data,arg.asType(dMythicMeta.class));
			}
		}
	
		if(!entry.hasObject(Statics.str_name)) {
			if(!entry.hasObject(Statics.str_name)) dB.echoError(entry.getResidingQueue(),String.format(str_error_parse,Statics.str_name));
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Element dmechanic_name=entry.getElement(Statics.str_name);
		Element dline=entry.getElement(Statics.str_line);
		
		String line=dline!=null?dline.asString():null;
		String mechanic_name=dline!=null?dmechanic_name.asString():null;
		
		if(mechanic_name!=null) {
			dMythicMechanic dmechanic=new dMythicMechanic(mechanic_name,line);
			if(!dmechanic.isPresent()) dB.echoError(entry.getResidingQueue(),String.format(str_error_execute,mechanic_name));
			entry.addObject(Statics.str_mechanic,dmechanic);
		} else {
			dB.echoError(entry.getResidingQueue(),String.format(str_error_parse,Statics.str_name));
		}
	}
}
