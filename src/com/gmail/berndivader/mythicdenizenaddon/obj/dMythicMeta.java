package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicMeta 
implements
dObject,
Adjustable {
	static Map<String,SkillMetadata>objects;
	public static String id;
	static MythicMobs mythicmobs;
	private String prefix;
	SkillMetadata meta;
	
	static {
		mythicmobs=MythicMobs.inst();
		objects=new HashMap<String,SkillMetadata>();
		id="mythicmeta@";
	}
	
	public dMythicMeta(String id) {
		this(objects.get(id));
	}

	public dMythicMeta(SkillMetadata data) {
		this.meta=data;
		if(data!=null) dMythicMeta.objects.put(this.identify(),this.meta);
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicMeta valueOf(String name) {
		return valueOf(name,null);
	}
	
	public static dList getEntityTargets(HashSet<AbstractEntity>entities) {
		dList list=new dList();
		for(AbstractEntity e:entities) {
			list.add(new dEntity(e.getBukkitEntity()).identify());
		}
		return list;
	}
	
	public static dList getLocationTargets(HashSet<AbstractLocation>locations) {
		dList list=new dList();
		for(AbstractLocation l:locations) {
			list.add(new dLocation(BukkitAdapter.adapt(l)).identify());
		}
		return list;
	}
	
	public SkillMetadata getSkillMetadata() {
		return this.meta;
	}
	
	@Override
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		int i1=a.attributes.length;
		if(i1>0) {
			String s1=a.getAttribute(i1).toLowerCase();
			switch(s1) {
				case "caster":
					return new dEntity(this.meta.getCaster().getEntity().getBukkitEntity()).getAttribute(a.fulfill(i1));
			}
		}
		return new Element(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element e1=m.getValue();
		switch (m.getName().toLowerCase()) {
			case "cause":
				break;
			case "caster":
				break;
			case "trigger":
				break;
			case "origin":
				break;
			case "targets":
				break;
			case "power":
				break;
		}
	}
	
	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicMeta";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return this.meta!=null?id+this.meta.hashCode():null;
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
	
    @Fetchable("mythicmeta")
    public static dMythicMeta valueOf(String name,TagContext context) {
       	if (dMythicMeta.objects.containsKey(name)) {
       		return new dMythicMeta(new SkillMetadata(dMythicMeta.objects.get(name)));
       	};
        return null;
    }
    
    @Override
    protected void finalize() throws Throwable {
    	dMythicMeta.objects.remove(this.identify());
    	super.finalize();
    }

	@Override
	public void applyProperty(Mechanism arg0) {
	}

}
