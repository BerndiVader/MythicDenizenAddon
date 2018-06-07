package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dLocationExt extends dObjectExtension {
	private dLocation location;
	
	public dLocationExt (dLocation l) {
		this.location=l;
	}
	
	public static boolean describes(dObject object) {
        return object instanceof dLocation;
    }
    
    public static dLocationExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dLocationExt((dLocation)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	if (a.startsWith("mmtargets")||a.startsWith("mm_targeter") && a.hasContext(1)) {
    		return MythicMobsAddon.getTargetsFor(location.clone(), a.getContext(1)).getAttribute(a.fulfill(1));
    	}
    	return null;
    }

    @Override
	public void adjust(Mechanism m) {
    	
		
	}
}
