package com.gmail.berndivader.mmDenizenAddon.plugins;


import com.gmail.berndivader.mmDenizenAddon.dObjectExtension;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dActiveMobExt extends dObjectExtension {
	private dEntity entity;
	
	
    public dActiveMobExt(dEntity e) {
    	this.entity = e;
	}

	public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }
    
    public static dActiveMobExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dActiveMobExt((dEntity)o);
    }
    
	
    @Override
    public String getAttribute(Attribute a) {

    	if (a.startsWith("isactivemob")) {
    		return new Element(MythicMobsAddon.isActiveMob(entity.getUUID())).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("isdead")) {
    		return new Element(MythicMobsAddon.isDead(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("hasthreattable")) {
    		return new Element(MythicMobsAddon.hasThreatTable(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("hasmythicspawner")) {
    		return new Element(MythicMobsAddon.hasMythicSpawner(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("activemob") && MythicMobsAddon.isActiveMob(entity.getUUID())) {
    		return new dActiveMob(MythicMobsAddon.getActiveMob(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
    	}
        return null;
    }
}
