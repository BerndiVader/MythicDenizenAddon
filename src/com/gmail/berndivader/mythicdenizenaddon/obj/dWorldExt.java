package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizencore.objects.Element;
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
		return new Element(this.world!=null?this.world.identify():null).getAttribute(a.fulfill(0));
    }

    @Override
	public void adjust(Mechanism m) {
    	
		
	}
}
