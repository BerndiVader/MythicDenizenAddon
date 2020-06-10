package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.AbstractMap;
import java.util.HashSet;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
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

public 
class
CreateMythicMeta
extends
AbstractCommand 
{
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if(!entry.hasObject(Statics.str_cause)&&arg.matchesPrefix(Statics.str_cause)) {
				entry.addObject(Statics.str_cause,arg.asElement());
			} else if(!entry.hasObject(Statics.str_caster)&&arg.matchesPrefix(Statics.str_caster)) {
				entry.addObject(Statics.str_caster,arg.matchesArgumentType(EntityTag.class)
						?arg.asType(EntityTag.class)
								:arg.matchesArgumentType(dActiveMob.class)
								?arg.asType(dActiveMob.class)
										:null);
			} else if(!entry.hasObject(Statics.str_trigger)&&arg.matchesPrefix(Statics.str_trigger)&&arg.matchesArgumentType(EntityTag.class)) {
				entry.addObject(Statics.str_trigger,arg.asType(EntityTag.class));
			} else if(!entry.hasObject(Statics.str_origin)&&arg.matchesPrefix(Statics.str_origin)&&arg.matchesArgumentType(LocationTag.class)) {
				entry.addObject(Statics.str_origin,arg.asType(LocationTag.class));
			} else if(!entry.hasObject(Statics.str_targets)&&arg.matchesPrefix(Statics.str_targets)&&arg.matchesArgumentType(ListTag.class)) {
				entry.addObject(Statics.str_targets,arg.asType(ListTag.class));
			} else if(!entry.hasObject(Statics.str_power)&&arg.matchesPrefix(Statics.str_power)&&arg.matchesFloat()) {
				entry.addObject(Statics.str_power,arg.asElement());
			} else if(!entry.hasObject(Statics.str_result)&&arg.matchesPrefix(Statics.str_result)) {
				entry.addObject(Statics.str_result,arg.asElement());
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) {
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
			caster=new GenericCaster(BukkitAdapter.adapt(((EntityTag)entry.getObjectTag(Statics.str_caster)).getBukkitEntity()));
		}
		if(entry.hasObject(Statics.str_trigger)) {
			trigger=BukkitAdapter.adapt(((EntityTag)entry.getObjectTag(Statics.str_trigger)).getBukkitEntity());
		}
		if(entry.hasObject(Statics.str_origin)) {
			origin=BukkitAdapter.adapt(((LocationTag)entry.getObjectTag(Statics.str_origin)));
		}
		if(entry.hasObject(Statics.str_power)) {
			power=entry.getElement(Statics.str_power).asFloat();
		}
		if(entry.hasObject(Statics.str_targets)) {
			AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>>pair=Utils.split_target_list((ListTag)entry.getObject(Statics.str_targets));
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
