package com.gmail.berndivader.mythicdenizenaddon.conditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.berndivader.mythicdenizenaddon.context.MythicContextSource;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import net.aufdemrand.denizencore.exceptions.ScriptEntryCreationException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.aH.Argument;
import net.aufdemrand.denizencore.objects.aH.PrimitiveType;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.dScript;
import net.aufdemrand.denizencore.scripts.ScriptBuilder;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.core.DetermineCommand;
import net.aufdemrand.denizencore.scripts.queues.ScriptQueue;
import net.aufdemrand.denizencore.scripts.queues.core.InstantQueue;

public
class 
ScriptCondition<E extends dObject>
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
			context.put("source",denizen_source);
			if(denizen_target!=null) context.put("target",denizen_target);
			
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
					if(arg.matchesPrimitive(PrimitiveType.Boolean)) {
						match=arg.asElement().asBoolean();
						break;
					}
				}
			}
			
		}
		return match;
	}
}
