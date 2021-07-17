package com.gmail.berndivader.mythicdenizenaddon.mechanics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.DurationTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.denizenscript.denizencore.scripts.queues.core.TimedQueue;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
DenizenScriptMechanic
extends
SkillMechanic 
implements
INoTargetSkill,
ITargetedLocationSkill,
ITargetedEntitySkill
{
	final String script_name;
	HashMap<String,String>attributes;

	public DenizenScriptMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = ThreadSafetyLevel.EITHER;;
		
		script_name=config.getString("script","");
		attributes=Utils.parse_attributes(skill);

	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		return this._cast(data,Optional.ofNullable(abstract_entity),Optional.empty());
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation abstract_location) {
		return this._cast(data,Optional.empty(),Optional.ofNullable(abstract_location));
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return this._cast(data,Optional.empty(),Optional.empty());
	}
	
	boolean _cast(SkillMetadata data,Optional<AbstractEntity>o_target_entity,Optional<AbstractLocation>o_target_location) {
		ScriptTag script=new ScriptTag(script_name);
		List<ScriptEntry>entries=null;
		if(script!=null&&script.isValid()) {
			try {
				ScriptEntry entry=new ScriptEntry(script.getName(),new String[0],script.getContainer());
				entry.setScript(script_name);
				entries=script.getContainer().getBaseEntries(entry.entryData.clone());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(entries==null) return false;
		ScriptQueue queue;
		if(script.getContainer().contains("SPEED")) {
			long ticks=DurationTag.valueOf(script.getContainer().getString("SPEED","0")).getTicks();
			queue=ticks>0?((TimedQueue)(new TimedQueue(script.getContainer().getName()).addEntries(entries))).setSpeed(ticks):new InstantQueue(script.getContainer().getName()).addEntries(entries);
		} else {
			queue=new TimedQueue(script.getContainer().getName()).addEntries(entries);
		}
		HashMap<String,ObjectTag>context=new HashMap<String,ObjectTag>();
		context.put("data",new dMythicMeta(data));
		context.put("target",o_target_entity.isPresent()?new EntityTag(o_target_entity.get().getBukkitEntity()):o_target_location.isPresent()?new LocationTag(BukkitAdapter.adapt(o_target_location.get())):null);
		queue.setContextSource(new MythicContextSource(context));
		for(Map.Entry<String,String>item:attributes.entrySet()) {
			queue.addDefinition(item.getKey(),item.getValue());
		}
		queue.start();
		return true;
	}


}
