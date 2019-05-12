package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.AbstractMap;
import java.util.HashSet;

import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public 
class
CreateMythicMeta
extends
AbstractCommand 
{
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(Statics.str_cause)&&arg.matchesPrefix(Statics.str_cause)) {
				entry.addObject(Statics.str_cause,arg.asElement());
			} else if(!entry.hasObject(Statics.str_caster)&&arg.matchesPrefix(Statics.str_caster)) {
				entry.addObject(Statics.str_caster,arg.matchesArgumentType(dEntity.class)
						?arg.asType(dEntity.class)
								:arg.matchesArgumentType(dActiveMob.class)
								?arg.asType(dActiveMob.class)
										:null);
			} else if(!entry.hasObject(Statics.str_trigger)&&arg.matchesPrefix(Statics.str_trigger)&&arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Statics.str_trigger,arg.asType(dEntity.class));
			} else if(!entry.hasObject(Statics.str_origin)&&arg.matchesPrefix(Statics.str_origin)&&arg.matchesArgumentType(dLocation.class)) {
				entry.addObject(Statics.str_origin,arg.asType(dLocation.class));
			} else if(!entry.hasObject(Statics.str_targets)&&arg.matchesPrefix(Statics.str_targets)&&arg.matchesArgumentType(dList.class)) {
				entry.addObject(Statics.str_targets,arg.asType(dList.class));
			} else if(!entry.hasObject(Statics.str_power)&&arg.matchesPrefix(Statics.str_power)&&arg.matchesPrimitive(PrimitiveType.Float)) {
				entry.addObject(Statics.str_power,arg.asElement());
			} else if(!entry.hasObject(Statics.str_result)&&arg.matchesPrefix(Statics.str_result)) {
				entry.addObject(Statics.str_result,arg.asElement());
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		GenericCaster caster=null;
		AbstractEntity trigger=null;
		SkillTrigger cause=SkillTrigger.API;
		AbstractLocation origin=null;
		HashSet<AbstractEntity>entity_targets=new HashSet<>();
		HashSet<AbstractLocation>location_targets=new HashSet<>();
		float power=1f;
		String result=Statics.str_result;
		
		if(entry.hasObject(Statics.str_cause)) {
			if((cause=Utils.enum_lookup(SkillTrigger.class,entry.getElement(Statics.str_cause).asString().toUpperCase()))==null) cause=SkillTrigger.API;
		}
		if(entry.hasObject(Statics.str_caster)) {
			caster=new GenericCaster(BukkitAdapter.adapt(((dEntity)entry.getdObject(Statics.str_caster)).getBukkitEntity()));
		}
		if(entry.hasObject(Statics.str_trigger)) {
			trigger=BukkitAdapter.adapt(((dEntity)entry.getdObject(Statics.str_trigger)).getBukkitEntity());
		}
		if(entry.hasObject(Statics.str_origin)) {
			origin=BukkitAdapter.adapt(((dLocation)entry.getdObject(Statics.str_origin)));
		}
		if(entry.hasObject(Statics.str_power)) {
			power=entry.getElement(Statics.str_power).asFloat();
		}
		if(entry.hasObject(Statics.str_targets)) {
			AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>>pair=Utils.split_target_list((dList)entry.getObject(Statics.str_targets));
			entity_targets=pair.getKey();
			location_targets=pair.getValue();
			if(entity_targets.size()>0) location_targets=null;
		}
		if(entry.hasObject(Statics.str_result)) {
			result=entry.getElement(Statics.str_result).asString();
		}
		entry.addObject(result,new dMythicMeta(new SkillMetadata(cause,caster,trigger,origin,entity_targets,location_targets,power)));
	}
	
}
