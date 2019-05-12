package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.AbstractMap;
import java.util.HashSet;

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
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

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
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject(Statics.str_skill)&&arg.matchesPrefix(Statics.str_skill)) {
				if(arg.matchesArgumentType(dMythicSkill.class)) {
					entry.addObject(Statics.str_skill,arg.asType(dMythicSkill.class));
				} else if (arg.matchesPrimitive(PrimitiveType.String)) {
					entry.addObject(Statics.str_skill,new dMythicSkill(arg.asElement().asString()));
				}
			} else if(!entry.hasObject(Statics.str_data)&&arg.matchesPrefix(Statics.str_data)) {
				if(arg.matchesArgumentType(dMythicMeta.class)) entry.addObject(Statics.str_data,arg.asType(dMythicMeta.class));
			} else if(!entry.hasObject(Statics.str_caster)&&arg.matchesPrefix(Statics.str_caster)) {
				if(arg.matchesArgumentType(dEntity.class)) entry.addObject(Statics.str_caster,arg.asType(dEntity.class));
			} else if(!entry.hasObject(Statics.str_trigger)&&arg.matchesPrefix(Statics.str_trigger)) {
				if(arg.matchesArgumentType(dEntity.class)) entry.addObject(Statics.str_trigger,arg.asType(dEntity.class));
			} else if(!entry.hasObject(Statics.str_origin)&&arg.matchesPrefix(Statics.str_origin)) {
				if(arg.matchesArgumentType(dLocation.class)) entry.addObject(Statics.str_origin,arg.asType(dLocation.class));
			} else if(!entry.hasObject(Statics.str_cause)&&arg.matchesPrefix(Statics.str_cause)) {
				if(arg.matchesPrimitive(PrimitiveType.String)) entry.addObject(Statics.str_cause,arg.asElement());
			} else if(!entry.hasObject(Statics.str_power)&&arg.matchesPrefix(Statics.str_power)) {
				if(arg.matchesPrimitive(PrimitiveType.Float)) arg.asElement();
			} else if(!entry.hasObject(Statics.str_targets)&&arg.matchesPrefix(Statics.str_targets)) {
				if(arg.matchesArgumentType(dList.class)) {
					entry.addObject(Statics.str_targets,arg.asType(dList.class));
				} else if(arg.matchesArgumentType(dEntity.class)) {
					dList list=new dList();
					list.addObject(arg.asType(dEntity.class));
					entry.addObject(Statics.str_targets,list);
				}
			}
		}
		
		if(!entry.hasObject(Statics.str_skill)) dB.echoError(entry.getResidingQueue(),String.format(str_error,Statics.str_skill));
		
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		SkillMetadata meta=null;
		dMythicSkill dskill=entry.getdObject(Statics.str_skill);
		if(entry.hasObject(Statics.str_data)) {
			meta=((dMythicMeta)entry.getdObject(Statics.str_data)).getSkillMetadata();
		} else {
			GenericCaster caster=null;
			AbstractEntity trigger=null;
			AbstractLocation origin=null;
			SkillTrigger cause=SkillTrigger.API;
			float power=1f;
			HashSet<AbstractLocation>locations=new HashSet<>();
			HashSet<AbstractEntity>entities=new HashSet<>();
			
			if(dskill.hasMeta()) meta=dskill.getSkillMetadata();
			
			dEntity dentity_caster=(dEntity)entry.getdObject(Statics.str_caster);
			dEntity dentity_trigger=(dEntity)entry.getdObject(Statics.str_trigger);
			dLocation dlocation_origin=(dLocation)entry.getdObject(Statics.str_origin);
			Element dcause=entry.getElement(Statics.str_cause);
			Element dpower=entry.getElement(Statics.str_power);
			dList dtargets=(dList)entry.getdObject(Statics.str_targets);
			
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
