package com.gmail.berndivader.mythicdenizenaddon.context;

import java.util.HashMap;

import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.queues.ContextSource;

public 
class 
MythicContextSource
implements
ContextSource
{
	HashMap<String,ObjectTag>context_map;
	
	public MythicContextSource(HashMap<String,ObjectTag>context_map) {
		this.context_map=new HashMap<>();
		this.context_map.putAll(context_map);
	}
	
	@Override
	public ObjectTag getContext(String id) {
		id=id.toLowerCase();
		if(this.context_map.containsKey(id.toLowerCase())) {
			return this.context_map.get(id);
		}
		return null;
	}

	@Override
	public boolean getShouldCache() {
		return false;
	}
	
	public HashMap<String,ObjectTag> getContexts() {
		return this.context_map;
	}

}
