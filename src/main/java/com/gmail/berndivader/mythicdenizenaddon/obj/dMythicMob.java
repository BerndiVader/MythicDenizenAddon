package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicMob 
implements 
dObject
{
	static String id="mythicmob@";
	static MythicMobs mythicmobs=MythicMobs.inst();

	String prefix;
	MythicMob mm;

	public dMythicMob(MythicMob mythicMob) {
		this.mm = mythicMob;
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dMythicMob valueOf(String name) {
		return valueOf(name, null);
	}
	
	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		return new Element(identify()).getAttribute(a);
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicMob";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return this.mm!=null?id+this.mm.getInternalName():null;
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
	
	@Override
	public String toString() {
		return this.identify();
	}

    @Fetchable("mythicmob")
    public static dMythicMob valueOf(String uniqueName, TagContext context) {
        if (uniqueName==null) return null;
        try {
            uniqueName=uniqueName.replace(id,"");
            if (!MythicMobsAddon.isMythicMob(uniqueName)) {
                return null;
            }
            return new dMythicMob(MythicMobsAddon.getMythicMob(uniqueName));
        }
        catch (Exception e) {
            return null;
        }
    }
}
