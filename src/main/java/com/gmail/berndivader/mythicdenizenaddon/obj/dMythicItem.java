package com.gmail.berndivader.mythicdenizenaddon.obj;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicItem 
implements
dObject
{
	public static String id="mythicitem@";
	private String prefix;
	MythicItem mi;
	static MythicMobs mythicmobs=MythicMobs.inst();

	public dMythicItem(String name) {
		this.mi=mythicmobs.getItemManager().getItem(name).orElse(null);
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicItem valueOf(String name) {
		return valueOf(name,null);
	}

	@Override
	public String toString() {
		return this.identify();
	}

	@Override
	public String getAttribute(Attribute a) {
		if(a.startsWith("internalname")||a.startsWith("type")) {
			return new Element(mi.getInternalName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("amount")) {
			return new Element(mi.getAmount()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("displayname")) {
			return new Element(mi.getDisplayName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("lore")) {
			dList dl=new dList();
			dl.addAll(mi.getLore());
			return dl.getAttribute(a.fulfill(1));
		} else if(a.startsWith("materialname")) {
			return new Element(mi.getMaterialName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("materialdata")) {
			return new Element(mi.getMaterialData()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("itemstack")||a.startsWith("get_item")) {
			return getItemstack().getAttribute(a.fulfill(1));
		} else if(a.startsWith("ispresent")) {
			return new Element(isPresent()).getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a);
	}
	
	public boolean isPresent() {
		return this.mi!=null;
	}
	
	private dItem getItemstack() {
		return new dItem(BukkitAdapter.adapt(mi.generateItemStack(1)));
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicItem";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return this.mi!=null?id+this.mi.getInternalName():null;
	}

	@Override
	public String identifySimple() {
		return identify();
	}

	@Override
	public boolean isUnique() {
		return false;
	}
	
	@Override
	public dObject setPrefix(String string) {
		this.prefix = string;
		return this;
	}
	
    @Fetchable("mythicitem")
    public static dMythicItem valueOf(String name,TagContext context) {
        if (name==null||name.isEmpty()) return null;
        try {
            return new dMythicItem(name.replace(id,""));
        }
        catch (Exception e) {
        	dB.echoError(e.getMessage());
        }
        return null;
    }

}
