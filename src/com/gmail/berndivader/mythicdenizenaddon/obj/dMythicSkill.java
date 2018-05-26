package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.HashSet;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
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

public class dMythicSkill 
implements
dObject,
Adjustable {
	static String id="mythicskill@";
	static MythicMobs mythicmobs=MythicMobs.inst();
	private String prefix;
	
	Skill skill;
	SkillMetadata data;
	HashSet<AbstractEntity>eTargets;
	HashSet<AbstractLocation>lTargets;
	float power;
	AbstractLocation origin;
	AbstractEntity trigger,caster;
	SkillTrigger cause;

	public dMythicSkill(String name) {
		if ((this.skill=mythicmobs.getSkillManager().getSkill(name).get())==null) {
			System.err.println("MythicSkill not present!");
			return;
		}
		this.eTargets=new HashSet<>();
		this.lTargets=new HashSet<>();
		this.cause=SkillTrigger.API;
		this.power=1F;
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

	@Override
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		if(a.attributes.size()>0) {
			String s1=a.attributes.get(0).toLowerCase();
			switch(s1) {
				case "type":
					if (skill!=null) return new Element(skill.getInternalName()).asString();
					break;
				case "entities":
					if (this.eTargets!=null) return getEntityTargets(this.eTargets).getAttribute(a.fulfill(1));
					break;
				case "locations":
					if (this.lTargets!=null) return getLocationTargets(this.lTargets).getAttribute(a.fulfill(1));
					break;
				case "power":
					return new Element(this.power).getAttribute(a.fulfill(1));
				case "origin":
					if (this.origin!=null) new dLocation(BukkitAdapter.adapt(this.origin)).getAttribute(a.fulfill(1));
					break;
				case "trigger":
					if (this.trigger!=null) return new dEntity(this.trigger.getBukkitEntity()).getAttribute(a.fulfill(1));
					break;
				case "caster":
					if (this.caster!=null) return new dEntity(this.caster.getBukkitEntity()).getAttribute(a.fulfill(1));
					break;
			}
		}
		return new Element(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
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
		return id+this.skill.getInternalName();
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
	
    @Fetchable("mythicskill")
    public static dMythicSkill valueOf(String name,TagContext context) {
        if (name==null||name.isEmpty()) return null;
        try {
        	name=name.replace(id,"");
            return new dMythicSkill(name);
        }
        catch (Exception e) {
        	System.err.println(e.getMessage());
        }
        return null;
    }

	@Override
	public void applyProperty(Mechanism arg0) {
	}
}
