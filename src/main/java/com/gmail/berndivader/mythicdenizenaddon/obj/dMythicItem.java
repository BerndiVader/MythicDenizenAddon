package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;

public 
class
dMythicItem 
implements
ObjectTag
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
		if(a==null) return null;
		if(a.startsWith("internalname")||a.startsWith("type")) {
			return new ElementTag(mi.getInternalName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("amount")) {
			return new ElementTag(mi.getAmount()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("displayname")) {
			return new ElementTag(mi.getDisplayName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("lore")) {
			ListTag dl=new ListTag();
			dl.addAll(mi.getLore());
			return dl.getAttribute(a.fulfill(1));
		} else if(a.startsWith("materialname")) {
			return new ElementTag(mi.getMaterialName()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("materialdata")) {
			return new ElementTag(mi.getMaterialData()).getAttribute(a.fulfill(1));
		} else if(a.startsWith("itemstack")||a.startsWith("get_item")) {
			return getItemstack().getAttribute(a.fulfill(1));
		} else if(a.startsWith("ispresent")) {
			return new ElementTag(isPresent()).getAttribute(a.fulfill(1));
		}
		return new ElementTag(identify()).getAttribute(a);
	}
	
	public boolean isPresent() {
		return this.mi!=null;
	}
	
	private ItemTag getItemstack() {
		return new ItemTag(BukkitAdapter.adapt(mi.generateItemStack(1)));
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
	public ObjectTag setPrefix(String string) {
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
        	Debug.echoError(e.getMessage());
        }
        return null;
    }

}
