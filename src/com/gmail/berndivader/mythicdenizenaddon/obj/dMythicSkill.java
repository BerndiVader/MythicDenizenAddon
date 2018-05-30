package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.HashSet;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dMythicSkill 
implements
dObject,
Adjustable {
	public static String id="mythicskill@";
	static MythicMobs mythicmobs=MythicMobs.inst();
	private String prefix;
	
	Skill skill;
	SkillMetadata meta;
	HashSet<AbstractEntity>eTargets;
	HashSet<AbstractLocation>lTargets;
	HashSet<GenericCaster>casters;
	float power;
	AbstractLocation origin;
	AbstractEntity trigger;
	GenericCaster caster;
	SkillTrigger cause;

	public dMythicSkill(String name) {
		if ((this.skill=mythicmobs.getSkillManager().getSkill(name).get())==null) {
			dB.log("MythicSkill "+name+" not present!");
		}
		this.eTargets=new HashSet<>();
		this.lTargets=new HashSet<>();
		this.casters=new HashSet<>();
		this.cause=SkillTrigger.API;
		this.power=1F;
		this.origin=null;
		this.trigger=null;
		this.caster=null;
		meta();
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicSkill valueOf(String name) {
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
	
	public boolean isPresent() {
		return this.skill!=null;
	}
	
	@Override
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		int i1=a.attributes.size();
		GenericCaster caster;
		AbstractEntity trigger;
		AbstractLocation origin;
		float power;
		if(a.attributes.size()>0) {
			String s1=a.attributes.get(0).toLowerCase();
			switch(s1) {
				case "present":
					return new Element(isPresent()).getAttribute(a.fulfill(i1));
				case "type":
					return new Element(isPresent()?skill.getInternalName():null).getAttribute(a.fulfill(i1));
				case "entities":
					return getEntityTargets(this.eTargets).getAttribute(a.fulfill(i1));
				case "locations":
					return getLocationTargets(this.lTargets).getAttribute(a.fulfill(i1));
				case "power":
					return new Element(this.power).getAttribute(a.fulfill(1));
				case "origin":
					if (this.origin!=null) return new dLocation(BukkitAdapter.adapt(this.origin)).getAttribute(a.fulfill(i1));
					break;
				case "trigger":
					if (this.trigger!=null) return new dEntity(this.trigger.getBukkitEntity()).getAttribute(a.fulfill(i1));
					break;
				case "caster":
					if (this.caster!=null) return new dEntity(this.caster.getEntity().getBukkitEntity()).getAttribute(a.fulfill(i1));
					break;
				case "useable":
					caster=this.caster;
					trigger=this.trigger;
					for(int i2=2;i2<=i1;i2++) {
						if(a.getAttribute(i2).startsWith("for")&&a.hasContext(i2)) {
							dEntity dentity=new Element(a.getContext(i2)).asType(dEntity.class);
							if (dentity!=null) caster=new GenericCaster(BukkitAdapter.adapt(dentity.getBukkitEntity()));
						} else if(a.getAttribute(i2).startsWith("with_trigger")&&a.hasContext(i2)) {
							dEntity dentity=new Element(a.getContext(i2)).asType(dEntity.class);
							if (dentity!=null) trigger=BukkitAdapter.adapt(dentity.getBukkitEntity());
						}
					}
					SkillMetadata data=new SkillMetadata(SkillTrigger.API,caster,trigger);
					return new Element(caster!=null&&skill.isUsable(data)).getAttribute(a.fulfill(i1));
				case "cast":
					trigger=this.trigger;
					origin=this.origin;
					power=this.power;
					HashSet<AbstractEntity>eTargets=new HashSet<>();
					HashSet<AbstractLocation>lTargets=new HashSet<>();
					HashSet<GenericCaster>casters=new HashSet<>();
					if (this.caster!=null) casters.add(this.caster);
					boolean bl1=false;
					if(i1>1) {
						for(int i2=2;i2<=i1;i2++) {
							if(a.getAttribute(i2).startsWith("for")&&a.hasContext(i2)) {
								casters.clear();
								if(!dList.matches(a.getContext(i2))&&dEntity.matches(a.getContext(i2))) {
									casters.add(new GenericCaster(BukkitAdapter.adapt(new Element(a.getContext(i2)).asType(dEntity.class).getBukkitEntity())));
								} else {
									dList targets=new Element(a.getContext(i2)).asType(dList.class);
									for(String s4:targets) {
										if (dEntity.matches(s4)) {
											casters.add(new GenericCaster(BukkitAdapter.adapt(new Element(s4).asType(dEntity.class).getBukkitEntity())));
										}
									}
								}
								dEntity dentity=new Element(a.getContext(i2)).asType(dEntity.class);
								if (dentity!=null) caster=new GenericCaster(BukkitAdapter.adapt(dentity.getBukkitEntity()));
							} else if(a.getAttribute(i2).startsWith("with_trigger")&&a.hasContext(i2)) {
								dEntity dentity=new Element(a.getContext(i2)).asType(dEntity.class);
								if (dentity!=null) trigger=BukkitAdapter.adapt(dentity.getBukkitEntity());
							} else if(a.getAttribute(i2).startsWith("with_origin")&&a.hasContext(i2)) {
								dLocation dlocation=new Element(a.getContext(i2)).asType(dLocation.class);
								if(dlocation!=null) origin=BukkitAdapter.adapt(dlocation.clone());
							} else if(a.getAttribute(i2).startsWith("with_power")&&a.hasContext(i2)) {
								power=(float)a.getDoubleContext(i2);
							} else if(a.getAttribute(i2).startsWith("at_targets")&&a.hasContext(i2)) {
								dList targets=new Element(a.getContext(i2)).asType(dList.class);
								for(String s4:targets) {
									if (dEntity.matches(s4)) {
										eTargets.add(BukkitAdapter.adapt(new Element(s4).asType(dEntity.class).getBukkitEntity()));
									} else if (dLocation.matches(s4)) {
										lTargets.add(BukkitAdapter.adapt(new Element(s4).asType(dLocation.class).clone()));
									}
								}
							}
						}
						for(GenericCaster gc:casters) {
							try {
								this.skill.execute(this.cause,gc,trigger,origin,eTargets,lTargets,power);
								bl1=true;
							} catch (Exception ex) {
								dB.log(ex.getMessage());
								bl1=false;
							}
						}
						return new Element(bl1).getAttribute(a.fulfill(i1));
					}
					if(bl1=(this.meta.getCaster()!=null)) {
						try {
							skill.execute(this.meta);
						} catch (Exception ex) {
							dB.log(ex.getMessage());
							bl1=false;
						}
					}
					return new Element(bl1).getAttribute(a.fulfill(i1));
			}
		}
		return new Element(identify()).getAttribute(a.fulfill(0));
	}
	
	boolean meta() {
		return (this.meta=new SkillMetadata(this.cause,this.caster,this.trigger,this.origin,this.eTargets,this.lTargets,this.power))!=null;
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element e1=m.getValue();
		switch (m.getName().toLowerCase()) {
			case "cause":
				try {
					this.cause=SkillTrigger.valueOf(e1.asString().toUpperCase());
				} catch (Exception ex) {
					dB.log(ex.getMessage());
					this.cause=SkillTrigger.API;
				}
				break;
			case "caster":
				if (e1.matchesType(dEntity.class)) this.caster=new GenericCaster(BukkitAdapter.adapt(e1.asType(dEntity.class).getBukkitEntity()));
				break;
			case "trigger":
				if (e1.matchesType(dEntity.class)) this.trigger=BukkitAdapter.adapt(e1.asType(dEntity.class).getBukkitEntity());
				break;
			case "origin":
				if (e1.matchesType(dLocation.class)) this.origin=BukkitAdapter.adapt(e1.asType(dLocation.class).clone());
				break;
			case "targets":
				dList targets = new dList();
				if (e1.matchesType(dList.class)) {
					targets=e1.asType(dList.class);
				} else {
					targets.add(e1.matchesType(dEntity.class)?e1.asType(dEntity.class).identify():e1.matchesType(dLocation.class)?e1.asType(dLocation.class).identify():null);
				}
				this.eTargets.clear();
				this.lTargets.clear();
				for(String s4:targets) {
					if (dEntity.matches(s4)) {
						eTargets.add(BukkitAdapter.adapt(new Element(s4).asType(dEntity.class).getBukkitEntity()));
					} else if (dLocation.matches(s4)) {
						lTargets.add(BukkitAdapter.adapt(new Element(s4).asType(dLocation.class).clone()));
					}
				}
				break;
			case "power":
				this.power=e1.isFloat()?e1.asFloat():power;
				break;
		}
	}
	
	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicSkill";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return this.skill!=null?id+this.skill.getInternalName():null;
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
	
    @Fetchable("mythicskill")
    public static dMythicSkill valueOf(String name,TagContext context) {
        if (name==null||name.isEmpty()) return null;
        try {
        	name=name.replace(id,"");
            return new dMythicSkill(name);
        }
        catch (Exception e) {
        	dB.log(e.getMessage());
        }
        return null;
    }

	@Override
	public void applyProperty(Mechanism arg0) {
	}
}
