package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.AbstractMap;
import java.util.HashSet;

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
	static String str_cause="cause",str_caster="caster",str_trigger="trigger",str_origin="origin",str_targets="targets",str_power="power",str_result="mythicmeta";
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(str_cause)&&arg.matchesPrefix(str_cause)) {
				entry.addObject(str_cause,arg.asElement());
			} else if(!entry.hasObject(str_caster)&&arg.matchesPrefix(str_caster)) {
				entry.addObject(str_caster,arg.matchesArgumentType(dEntity.class)
						?arg.asType(dEntity.class)
								:arg.matchesArgumentType(dActiveMob.class)
								?arg.asType(dActiveMob.class)
										:null);
			} else if(!entry.hasObject(str_trigger)&&arg.matchesPrefix(str_trigger)&&arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(str_trigger,arg.asType(dEntity.class));
			} else if(!entry.hasObject(str_origin)&&arg.matchesPrefix(str_origin)&&arg.matchesArgumentType(dLocation.class)) {
				entry.addObject(str_origin,arg.asType(dLocation.class));
			} else if(!entry.hasObject(str_targets)&&arg.matchesPrefix(str_targets)&&arg.matchesArgumentType(dList.class)) {
				entry.addObject(str_targets,arg.asType(dList.class));
			} else if(!entry.hasObject(str_power)&&arg.matchesPrefix(str_power)&&arg.matchesPrimitive(PrimitiveType.Float)) {
				entry.addObject(str_power,arg.asElement());
			} else if(!entry.hasObject(str_result)&&arg.matchesPrefix(str_result)) {
				entry.addObject(str_result,arg.asElement());
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
		String result=str_result;
		
		if(entry.hasObject(str_cause)) {
			if((cause=Utils.enum_lookup(SkillTrigger.class,entry.getElement(str_cause).asString().toUpperCase()))==null) cause=SkillTrigger.API;
		}
		if(entry.hasObject(str_caster)) {
			caster=new GenericCaster(BukkitAdapter.adapt(((dEntity)entry.getdObject(str_caster)).getBukkitEntity()));
		}
		if(entry.hasObject(str_trigger)) {
			trigger=BukkitAdapter.adapt(((dEntity)entry.getdObject(str_trigger)).getBukkitEntity());
		}
		if(entry.hasObject(str_origin)) {
			origin=BukkitAdapter.adapt(((dLocation)entry.getdObject(str_origin)));
		}
		if(entry.hasObject(str_power)) {
			power=entry.getElement(str_power).asFloat();
		}
		if(entry.hasObject(str_targets)) {
			AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>>pair=Utils.split_target_list((dList)entry.getObject(str_targets));
			entity_targets=pair.getKey();
			location_targets=pair.getValue();
			if(entity_targets.size()>0) location_targets=null;
		}
		if(entry.hasObject(str_result)) {
			result=entry.getElement(str_result).asString();
		}
		entry.addObject(result,new dMythicMeta(new SkillMetadata(cause,caster,trigger,origin,entity_targets,location_targets,power)));
	}
	
}
