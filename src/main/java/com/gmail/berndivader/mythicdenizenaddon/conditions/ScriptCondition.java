package com.gmail.berndivader.mythicdenizenaddon.conditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.queues.ScriptQueue;
import com.denizenscript.denizencore.scripts.queues.core.InstantQueue;
import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

public
class 
ScriptCondition<E extends ObjectTag>
extends
AbstractCustomCondition
{
	static String str_determination="_determination";
	final String script_name;
	HashMap<String,String>attributes;

	public ScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		
		script_name=mlc.getString("script","");

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
	
	boolean __check(E denizen_source) {
		return __check(denizen_source,null);
	}
	
	boolean __check(E denizen_source, E denizen_target) {
		boolean match=false;
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

			HashMap<String,ObjectTag>context=new HashMap<String,ObjectTag>();
			context.put("source",denizen_source);
			if(denizen_target!=null) context.put("target",denizen_target);
			
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
			
			Object o=queue.getLastEntryExecuted().getArguments();
			if(o!=null&&o instanceof List) {
				for(String s1:(List<String>)o) {
					Argument arg=new Argument(s1);
					if(arg.matchesBoolean()) {
						match=arg.asElement().asBoolean();
						break;
					}
				}
			}
			
		}
		return match;
	}
}
