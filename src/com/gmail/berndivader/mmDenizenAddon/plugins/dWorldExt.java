package com.gmail.berndivader.mmDenizenAddon.plugins;

import com.gmail.berndivader.mmDenizenAddon.dObjectExtension;

import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dWorldExt extends dObjectExtension {
	private dWorld world;
	
	public dWorldExt (dWorld w) {
		this.world = w;
	}
	
	public static boolean describes(dObject object) {
        return object instanceof dWorld;
    }
    
    public static dWorldExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dWorldExt((dWorld)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	
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
