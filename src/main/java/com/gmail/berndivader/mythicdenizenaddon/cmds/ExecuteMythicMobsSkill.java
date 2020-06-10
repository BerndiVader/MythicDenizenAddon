package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.AbstractMap;
import java.util.HashSet;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicSkill;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

public 
class 
ExecuteMythicMobsSkill 
extends
AbstractCommand 
{
	
	static SkillManager skillmanager=MythicMobsAddon.mythicmobs.getSkillManager();
	static String str_error="ExecuteMythicMobsSkill - argument %s is required!";
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if(!entry.hasObject(Statics.str_skill)&&arg.matchesPrefix(Statics.str_skill)) {
				if(arg.matchesArgumentType(dMythicSkill.class)) {
					entry.addObject(Statics.str_skill,arg.asType(dMythicSkill.class));
				} else {
					entry.addObject(Statics.str_skill,new dMythicSkill(arg.asElement().asString()));
				}
			} else if(!entry.hasObject(Statics.str_data)&&arg.matchesPrefix(Statics.str_data)) {
				if(arg.matchesArgumentType(dMythicMeta.class)) entry.addObject(Statics.str_data,arg.asType(dMythicMeta.class));
			} else if(!entry.hasObject(Statics.str_caster)&&arg.matchesPrefix(Statics.str_caster)) {
				if(arg.matchesArgumentType(EntityTag.class)) entry.addObject(Statics.str_caster,arg.asType(EntityTag.class));
			} else if(!entry.hasObject(Statics.str_trigger)&&arg.matchesPrefix(Statics.str_trigger)) {
				if(arg.matchesArgumentType(EntityTag.class)) entry.addObject(Statics.str_trigger,arg.asType(EntityTag.class));
			} else if(!entry.hasObject(Statics.str_origin)&&arg.matchesPrefix(Statics.str_origin)) {
				if(arg.matchesArgumentType(LocationTag.class)) entry.addObject(Statics.str_origin,arg.asType(LocationTag.class));
			} else if(!entry.hasObject(Statics.str_cause)&&arg.matchesPrefix(Statics.str_cause)) {
				entry.addObject(Statics.str_cause,arg.asElement());
			} else if(!entry.hasObject(Statics.str_power)&&arg.matchesPrefix(Statics.str_power)) {
				if(arg.matchesFloat()) arg.asElement();
			} else if(!entry.hasObject(Statics.str_targets)&&arg.matchesPrefix(Statics.str_targets)) {
				if(arg.matchesArgumentType(ListTag.class)) {
					entry.addObject(Statics.str_targets,arg.asType(ListTag.class));
				} else if(arg.matchesArgumentType(EntityTag.class)) {
					ListTag list=new ListTag();
					list.addObject(arg.asType(EntityTag.class));
					entry.addObject(Statics.str_targets,list);
				}
			}
		}
		
		if(!entry.hasObject(Statics.str_skill)) Debug.echoError(entry.getResidingQueue(),String.format(str_error,Statics.str_skill));
		
	}
	
	@Override
	public void execute(ScriptEntry entry) {
		SkillMetadata meta=null;
		dMythicSkill dskill=entry.getObjectTag(Statics.str_skill);
		if(entry.hasObject(Statics.str_data)) {
			meta=((dMythicMeta)entry.getObjectTag(Statics.str_data)).getSkillMetadata();
		} else {
			GenericCaster caster=null;
			AbstractEntity trigger=null;
			AbstractLocation origin=null;
			SkillTrigger cause=SkillTrigger.API;
			float power=1f;
			HashSet<AbstractLocation>locations=new HashSet<>();
			HashSet<AbstractEntity>entities=new HashSet<>();
			
			if(dskill.hasMeta()) meta=dskill.getSkillMetadata();
			
			EntityTag dentity_caster=(EntityTag)entry.getObjectTag(Statics.str_caster);
			EntityTag dentity_trigger=(EntityTag)entry.getObjectTag(Statics.str_trigger);
			LocationTag dlocation_origin=(LocationTag)entry.getObject(Statics.str_origin);
			ElementTag dcause=entry.getElement(Statics.str_cause);
			ElementTag dpower=entry.getElement(Statics.str_power);
			ListTag dtargets=(ListTag)entry.getObjectTag(Statics.str_targets);
			
			if(dentity_caster!=null)caster=new GenericCaster(BukkitAdapter.adapt(dentity_caster.getBukkitEntity()));
			if(dentity_trigger!=null)trigger=BukkitAdapter.adapt(dentity_trigger.getBukkitEntity());
			if(dlocation_origin!=null)origin=BukkitAdapter.adapt(dlocation_origin);
			if(dcause!=null) cause=Utils.enum_lookup(SkillTrigger.class,dcause.asString());
			if(dpower!=null) power=dpower.asFloat();
			if(dtargets!=null) {
				AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>>pair=Utils.split_target_list(dtargets);
				locations=pair.getValue();
				entities=pair.getKey();
			}
			
			if(meta==null) {
				meta=new SkillMetadata(cause,caster,trigger,origin,entities,locations,power);
			} else {
				if(caster!=null)meta.setCaster(caster);
				if(trigger!=null)meta.setTrigger(trigger);
				if(origin!=null)meta.setOrigin(origin);
				if(!locations.isEmpty()) {
					meta.setLocationTargets(locations);
				} else if(!entities.isEmpty()) {
					meta.setEntityTargets(entities);
				}
			}
		}
		dskill.execute(meta);
	}
}
