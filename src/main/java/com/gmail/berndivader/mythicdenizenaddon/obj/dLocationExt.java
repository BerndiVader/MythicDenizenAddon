package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

public 
class
dLocationExt
extends 
dObjectExtension 
{
	private LocationTag location;
	
	public dLocationExt (LocationTag l) {
		this.location=l;
	}
	
	public static boolean describes(ObjectTag object) {
        return object instanceof LocationTag;
    }
    
    public static dLocationExt getFrom(ObjectTag o) {
    	if (!describes(o)) return null;
    	return new dLocationExt((LocationTag)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	if(a==null) return null;
    	if (a.startsWith("mmtargets")||a.startsWith("mm_targeter") && a.hasContext(1)) {
    		return MythicMobsAddon.getTargetsFor(location.clone(), a.getContext(1)).getAttribute(a.fulfill(1));
    	}
    	return null;
    }

    @Override
	public void adjust(Mechanism m) {
    	
		
	}
}
