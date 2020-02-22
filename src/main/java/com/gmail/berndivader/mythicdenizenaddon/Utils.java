package com.gmail.berndivader.mythicdenizenaddon;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptBuilder;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;

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
	
	public static AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>> split_target_list(ListTag list) {
		HashSet<AbstractLocation>location_targets=new HashSet<>();
		HashSet<AbstractEntity>entity_targets=new HashSet<>();
		int size=list.size();
		for(int i1=0;i1<size;i1++) {
			ElementTag e=(ElementTag)list.getObject(i1);
			if(e.matchesType(LocationTag.class)) {
				location_targets.add(BukkitAdapter.adapt((Location)e.asType(LocationTag.class,null)));
			} else if (e.matchesType(EntityTag.class)) {
				entity_targets.add(BukkitAdapter.adapt(((EntityTag)e.asType(EntityTag.class,null)).getBukkitEntity()));
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
	
	public static ListTag getTargetsForScriptTargeter(SkillMetadata data,String script_name,HashMap<String,String>attributes) {
		ScriptEntry entry=null;
		List<ScriptEntry>entries=null;
		ScriptTag script=new ScriptTag(script_name);
		if(script!=null&&script.isValid()) {
			try {
				entry=new ScriptEntry(script.getName(),new String[0],script.getContainer());
				entry.setScript(script_name);
				entries=script.getContainer().getBaseEntries(entry.entryData.clone());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(entries!=null) {
			String id=ScriptQueue.getNextId(script.getContainer().getName());
			long req_id=0l;
			ScriptBuilder.addObjectToEntries(entries,"reqid",req_id);

			HashMap<String,ObjectTag>context=new HashMap<String,ObjectTag>();
			context.put("data",new dMythicMeta(data));
			
			ScriptQueue queue=InstantQueue.getQueue(id).addEntries(entries);
			queue.setContextSource(new MythicContextSource(context));
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
				for(String s1:(List<String>)o) {
					Argument arg=new Argument(s1);
					if (arg.matchesArgumentType(ListTag.class)) {
						return arg.getList();
					}
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 
	 * @param targeter_string {@link String}
	 * @return skill_targeter {@link SkillTargeter}
	 */
	
	public static SkillTargeter parseSkillTargeter(String targeter_string) {
        String search = targeter_string.substring(1);
        MythicLineConfig mlc = new MythicLineConfig(search);
        String name = search.contains("{") ? search.substring(0, search.indexOf("{")) : search;
        return SkillTargeter.getMythicTargeter(name, mlc);
	}
	
	

}
