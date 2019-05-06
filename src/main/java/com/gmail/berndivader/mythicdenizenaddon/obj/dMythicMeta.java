package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
Adjustable
{
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
		int i1=1;
		if (a.startsWith("caster")) {
			return new dEntity(this.meta.getCaster().getEntity().getBukkitEntity()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("cause")) {
			return new Element(this.meta.getCause().toString()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("trigger")) {
			return new dEntity(this.meta.getTrigger().getBukkitEntity()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("origin")) {
			return new dLocation(BukkitAdapter.adapt(this.meta.getOrigin())).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("targets")) {
			dList targets=new dList();
			if(this.meta.getEntityTargets()!=null&&!this.meta.getEntityTargets().isEmpty()) {
				Iterator<AbstractEntity> it=this.meta.getEntityTargets().iterator();
				while(it.hasNext()) {
					targets.addObject(new dEntity(it.next().getBukkitEntity()));
				}
			} else if(this.meta.getLocationTargets()!=null&&!this.meta.getLocationTargets().isEmpty()) {
				Iterator<AbstractLocation>it=this.meta.getLocationTargets().iterator();
				while(it.hasNext()) {
					targets.addObject(new dLocation(BukkitAdapter.adapt(it.next())));
				}
			}
			return targets.getAttribute(a.fulfill(i1));
		} else if(a.startsWith("power")) {
			return new Element(this.meta.getPower()).getAttribute(a.fulfill(i1));
		}
		return new Element(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
		switch (m.getName().toLowerCase()) {
			case "cancel":
				this.meta.cancelEvent();
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
		if(this.meta!=null) return id+this.meta.hashCode();
		return null;
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
		this.prefix=string;
		return this;
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicMeta valueOf(String name) {
		return valueOf(name,null);
	}	
	
    @Fetchable("mythicmeta")
    public static dMythicMeta valueOf(String name,TagContext context) {
    	if(name==null) return null;
    	
       	if (dMythicMeta.objects.containsKey(name)) {
       		SkillMetadata data=dMythicMeta.objects.get(name);
       		return new dMythicMeta(data);
       	};
        return null;
    }
    
	@Override
	public String toString() {
		return identify();
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
