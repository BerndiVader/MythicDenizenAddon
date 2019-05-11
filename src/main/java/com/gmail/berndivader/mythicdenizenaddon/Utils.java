package com.gmail.berndivader.mythicdenizenaddon;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.objects.aH.Argument;
import net.aufdemrand.denizencore.scripts.ScriptBuilder;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.core.DetermineCommand;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;

public 
class 
Utils 
{
	
	public static <E extends Enum<E>> E enum_lookup(Class<E> e, String id) {
		E result;
		try {
			result=Enum.valueOf(e,id);
		} catch (IllegalArgumentException e1) {
			result=null;
		}
		return result;
	}
	
	public static AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>> split_target_list(dList list) {
		HashSet<AbstractLocation>location_targets=new HashSet<>();
		HashSet<AbstractEntity>entity_targets=new HashSet<>();
		int size=list.size();
		for(int i1=0;i1<size;i1++) {
			Element e=(Element)list.getObject(i1);
			if(e.matchesType(dLocation.class)) {
				location_targets.add(BukkitAdapter.adapt((Location)e.asType(dLocation.class)));
			} else if (e.matchesType(dEntity.class)) {
				entity_targets.add(BukkitAdapter.adapt(((dEntity)e.asType(dEntity.class)).getBukkitEntity()));
			}
		}
		return new AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>>(entity_targets,location_targets);
	}
	
	public static HashMap<String,String> parse_attributes(String line) {
		if(line.contains("{")&&line.contains("}")) {
			String parse[]=line.split("\\{")[1].split("\\}")[0].split(";");
			HashMap<String,String>attributes=new HashMap<>();
			int size=parse.length;
			for(int i1=0;i1<size;i1++) {
				if(parse[i1].startsWith("script")) continue;
				String arr1[]=parse[i1].split("=");
				if(arr1.length==2) attributes.put(arr1[0],arr1[1]);
			}
			return attributes;
		}
		return null;
	}
	
	static String str_determination="_determination";
	
	public static dList getTargetsForScriptTargeter(SkillMetadata data,String script_name,HashMap<String,String>attributes) {
		ScriptEntry entry=null;
		List<ScriptEntry>entries=null;
		dScript script=new dScript(script_name);
		if(script!=null&&script.isValid()) {
			try {
				entry=new ScriptEntry(script.getName(),new String[0],script.getContainer());
				entry.setScript(script_name);
				entries=script.getContainer().getBaseEntries(entry.entryData.clone());
			} catch (ScriptEntryCreationException e) {
				e.printStackTrace();
			}
		}
		
		if(entries!=null) {
			String id=ScriptQueue.getNextId(script.getContainer().getName());
			long req_id=DetermineCommand.getNewId();
			ScriptBuilder.addObjectToEntries(entries,"reqid",req_id);

			HashMap<String,dObject>context=new HashMap<String,dObject>();
			context.put("data",new dMythicMeta(data));
			
			ScriptQueue queue=InstantQueue.getQueue(id).addEntries(entries);
			queue.setContextSource(new MythicContextSource(context));
			queue.setReqId(req_id);
			for(Map.Entry<String,String>item:attributes.entrySet()) {
				queue.addDefinition(item.getKey(),item.getValue());
			}
			
			final ScriptEntry final_entry=entry;
			queue.callBack(new Runnable() {
				@Override
				public void run() {
					final_entry.setFinished(true);
				}
			});
			queue.start();
			
			while(!final_entry.isFinished) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			Object o=queue.getLastEntryExecuted().getArguments();
			if(o!=null&&o instanceof List) {
				@SuppressWarnings("unchecked")
				List<Argument>args=aH.interpret((List<String>)o);
				for(Argument arg:args) {
					if (arg.matchesArgumentType(dList.class)) {
						return arg.getList();
					}
				}
			}
			
		}
		return null;
	}
	

}
