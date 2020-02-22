package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Location;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.gmail.berndivader.mythicdenizenaddon.Utils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public
class
dMythicMeta 
implements
ObjectTag,
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
	
	public static ListTag getEntityTargets(HashSet<AbstractEntity>entities) {
		ListTag list=new ListTag();
		for(AbstractEntity e:entities) {
			list.add(new EntityTag(e.getBukkitEntity()).identify());
		}
		return list;
	}
	
	public static ListTag getLocationTargets(HashSet<AbstractLocation>locations) {
		ListTag list=new ListTag();
		for(AbstractLocation l:locations) {
			list.add(new LocationTag(BukkitAdapter.adapt(l)).identify());
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
			return new EntityTag(this.meta.getCaster().getEntity().getBukkitEntity()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("cause")) {
			return new ElementTag(this.meta.getCause().toString()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("trigger")) {
			return new EntityTag(this.meta.getTrigger().getBukkitEntity()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("origin")) {
			return new LocationTag(BukkitAdapter.adapt(this.meta.getOrigin())).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("targets")) {
			ListTag targets=new ListTag();
			if(this.meta.getEntityTargets()!=null&&!this.meta.getEntityTargets().isEmpty()) {
				Iterator<AbstractEntity> it=this.meta.getEntityTargets().iterator();
				while(it.hasNext()) {
					targets.addObject(new EntityTag(it.next().getBukkitEntity()));
				}
			} else if(this.meta.getLocationTargets()!=null&&!this.meta.getLocationTargets().isEmpty()) {
				Iterator<AbstractLocation>it=this.meta.getLocationTargets().iterator();
				while(it.hasNext()) {
					targets.addObject(new LocationTag(BukkitAdapter.adapt(it.next())));
				}
			}
			return targets.getAttribute(a.fulfill(i1));
		} else if(a.startsWith("power")) {
			return new ElementTag(this.meta.getPower()).getAttribute(a.fulfill(i1));
		}
		return new ElementTag(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
		switch (m.getName().toLowerCase()) {
			case "power":
				if(m.requireFloat()) this.meta.setPower(m.getValue().asFloat());
				break;
			case "origin":
				if(m.requireObject(LocationTag.class)) this.meta.setOrigin(BukkitAdapter.adapt((Location)m.getValue().asType(LocationTag.class,m.context)));
				break;
			case "cancel":
				this.meta.cancelEvent();
				break;
			case "caster":
				if(m.requireObject(EntityTag.class)) {
					this.meta.setCaster(new GenericCaster(BukkitAdapter.adapt(m.getValue().asType(EntityTag.class,m.context).getBukkitEntity())));
				}
				break;
			case "trigger":
				if(m.requireObject(EntityTag.class)) {
					this.meta.setCaster(new GenericCaster(BukkitAdapter.adapt(m.getValue().asType(EntityTag.class,m.context).getBukkitEntity())));
				}
				break;
			case "targets":
				if(m.requireObject(ListTag.class)) {
					HashSet<AbstractLocation>locations=new HashSet<>();
					HashSet<AbstractEntity>entities=new HashSet<>();
					ListTag list=m.getValue().asType(ListTag.class,m.context);
					AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>>pair=Utils.split_target_list(list);
					locations=pair.getValue();
					entities=pair.getKey();
					if(!locations.isEmpty()) {
						this.meta.setLocationTargets(locations);
					} else if(!entities.isEmpty()) {
						this.meta.setEntityTargets(entities);
					}
				}
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
	public ObjectTag setPrefix(String string) {
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
		// TODO Auto-generated method stub
		
	}

}
