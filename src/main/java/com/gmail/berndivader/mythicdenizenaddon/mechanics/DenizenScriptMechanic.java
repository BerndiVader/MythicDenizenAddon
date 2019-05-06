package com.gmail.berndivader.mythicdenizenaddon.mechanics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.objects.Duration;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.scripts.ScriptBuilder;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.core.DetermineCommand;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;
import net.aufdemrand.denizencore.scripts.queues.core.TimedQueue;

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
		this.ASYNC_SAFE=false;
		
		script_name=config.getString("script","");

		if(line.contains("{")&&line.contains("}")) {
			String parse[]=line.split("\\{")[1].split("\\}")[0].split(";");
			attributes=new HashMap<>();
			int size=parse.length;
			for(int i1=0;i1<size;i1++) {
				if(parse[i1].startsWith("script")) continue;
				String arr1[]=parse[i1].split("=");
				if(arr1.length==2) attributes.put(arr1[0],arr1[1]);
			}
		}
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
		dScript script=new dScript(script_name);
		List<ScriptEntry>entries=null;
		if(script!=null&&script.isValid()) {
			try {
				ScriptEntry entry=new ScriptEntry(script.getName(),new String[0],script.getContainer());
				entry.setScript(script_name);
				entries=script.getContainer().getBaseEntries(entry.entryData.clone());
			} catch (ScriptEntryCreationException e) {
				e.printStackTrace();
			}
		}
		if(entries==null) return false;
		ScriptQueue queue;
		String id=ScriptQueue.getNextId(script.getContainer().getName());
		long req_id=DetermineCommand.getNewId();
		ScriptBuilder.addObjectToEntries(entries,"reqid",req_id);
		if(script.getContainer().contains("SPEED")) {
			long ticks=Duration.valueOf(script.getContainer().getString("SPEED","0")).getTicks();
			queue=ticks>0?((TimedQueue)TimedQueue.getQueue(id).addEntries(entries)).setSpeed(ticks):InstantQueue.getQueue(id).addEntries(entries);
		} else {
			queue=TimedQueue.getQueue(id).addEntries(entries);
		}
		HashMap<String,dObject>context=new HashMap<String,dObject>();
		context.put("data",new dMythicMeta(data));
		context.put("target",o_target_entity.isPresent()?new dEntity(o_target_entity.get().getBukkitEntity()):null);
		context.put("location",o_target_location.isPresent()?new dLocation(BukkitAdapter.adapt(o_target_location.get())):null);
		queue.setContextSource(new MythicContextSource(context));
		queue.setReqId(req_id);
		for(Map.Entry<String,String>item:attributes.entrySet()) {
			queue.addDefinition(item.getKey(),item.getValue());
		}
		queue.start();
		return true;
	}


}
