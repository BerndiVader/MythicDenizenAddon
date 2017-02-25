package com.gmail.berndivader.mmDenizenAddon.plugins;

import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicSpawner implements dObject, Adjustable {
	private String prefix;
	MythicSpawner ms;

	public dMythicSpawner(MythicSpawner mythicSpawner) {
		this.ms = mythicSpawner;
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dMythicSpawner valueOf(String name) {
		return valueOf(name, null);
	}

	@Override
	public void adjust(Mechanism m) {
	}
	
	@Override
	public String getAttribute(Attribute arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void applyProperty(Mechanism arg0) {
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicSpawner";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return "mythicspawner@" + this.ms.getName();
	}

	@Override
	public String identifySimple() {
		return identify();
	}

	@Override
	public boolean isUnique() {
		return true;
	}
	
	@Override
	public dObject setPrefix(String string) {
		this.prefix = string;
		return this;
	}
	
    @Fetchable("mythicspawner")
    public static dMythicSpawner valueOf(String uniqueName, TagContext context) {
        if (uniqueName == null) {
            return null;
        }
        try {
            uniqueName = uniqueName.replace("mythicspawner@", "");
            if (!MythicMobsAddon.isMythicSpawner(uniqueName)) {
                return null;
            }
            return new dMythicSpawner(MythicMobsAddon.getMythicSpawner(uniqueName));
        }
        catch (Exception e) {
            return null;
        }
    }
}
