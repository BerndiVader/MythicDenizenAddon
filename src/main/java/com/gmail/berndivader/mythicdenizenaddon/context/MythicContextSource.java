package com.gmail.berndivader.mythicdenizenaddon.context;

import java.util.HashMap;

import net.aufdemrand.denizencore.interfaces.ContextSource;
import net.aufdemrand.denizencore.objects.dObject;

public 
class 
MythicContextSource
implements
ContextSource
{
	HashMap<String,dObject>context_map;
	
	public MythicContextSource(HashMap<String,dObject>context_map) {
		this.context_map=new HashMap<>();
		this.context_map.putAll(context_map);
	}
	
	@Override
	public dObject getContext(String id) {
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

}
