package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;


public 
class 
dWorldExt 
extends 
dObjectExtension 
{
	private WorldTag world;
	
	public dWorldExt (WorldTag w) {
		this.world = w;
	}
	
	public static boolean describes(ObjectTag object) {
        return object instanceof WorldTag;
    }
    
    public static dWorldExt getFrom(ObjectTag o) {
    	if (!describes(o)) return null;
    	return new dWorldExt((WorldTag)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	if(a==null) return null;
    	if (a.startsWith("allactivemobs")) {
    		return MythicMobsAddon.allActiveMobs(world.getWorld()).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("allmythicspawners")) {
    		return MythicMobsAddon.allMythicSpawners(world.getWorld()).getAttribute(a.fulfill(1));
    	}
    	return null;
    }

    @Override
	public void adjust(Mechanism m) {
    	
		
	}
}
