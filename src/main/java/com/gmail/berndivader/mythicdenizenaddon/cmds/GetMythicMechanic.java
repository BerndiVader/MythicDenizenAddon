package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMechanic;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

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
		for (Argument arg:entry.getProcessedArgs()) {
			if(!entry.hasObject(Statics.str_name)&&arg.matchesPrefix(Statics.str_name)) {
				entry.addObject(Statics.str_name,arg.asElement());
			} else if(!entry.hasObject(Statics.str_line)&&arg.matchesPrefix(Statics.str_line)) {
				entry.addObject(Statics.str_line,arg.asElement());
			} else if(!entry.hasObject(Statics.str_data)&&arg.matchesPrefix(Statics.str_data)) {
				entry.addObject(Statics.str_data,arg.asType(dMythicMeta.class));
			}
		}
	
		if(!entry.hasObject(Statics.str_name)) {
			if(!entry.hasObject(Statics.str_name)) Debug.echoError(entry.getResidingQueue(),String.format(str_error_parse,Statics.str_name));
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) {
		ElementTag dmechanic_name=entry.getElement(Statics.str_name);
		ElementTag dline=entry.getElement(Statics.str_line);
		
		String line=dline!=null?dline.asString():null;
		String mechanic_name=dline!=null?dmechanic_name.asString():null;
		
		if(mechanic_name!=null) {
			dMythicMechanic dmechanic=new dMythicMechanic(mechanic_name,line);
			if(!dmechanic.isPresent()) Debug.echoError(entry.getResidingQueue(),String.format(str_error_execute,mechanic_name));
			entry.addObject(Statics.str_mechanic,dmechanic);
		} else {
			Debug.echoError(entry.getResidingQueue(),String.format(str_error_parse,Statics.str_name));
		}
	}
}
