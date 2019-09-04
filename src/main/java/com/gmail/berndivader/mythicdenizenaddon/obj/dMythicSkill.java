package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.Optional;

import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.denizenscript.denizencore.utilities.debugging.Debug;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.skills.Skill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class 
dMythicSkill 
implements
ObjectTag,
Adjustable
{
	public static String id="mythicskill@";
	static MythicMobs mythicmobs=MythicMobs.inst();
	private String prefix;
	Skill skill;
	public Optional<String> metaId;
	dMythicMeta data;

	public dMythicSkill(String name) {
		this(name,null);
	}
	
	public dMythicSkill(String name,String meta_hash) {
		if (mythicmobs.getSkillManager().getSkill(name).isPresent()) {
			this.skill=mythicmobs.getSkillManager().getSkill(name).get();
		} else {
			Debug.echoError("MythicSkill "+name+" not present!");
		}
		if (((this.metaId=Optional.ofNullable(meta_hash)).isPresent())&&dMythicMeta.objects.containsKey(this.metaId.get())) {
			data=new dMythicMeta(dMythicMeta.objects.get(this.metaId.get()));
		}
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicSkill valueOf(String name) {
		return valueOf(name,null);
	}
	
	public boolean isPresent() {
		return this.skill!=null;
	}
	
	public boolean hasMeta() {
		return this.data!=null&&this.data.meta!=null;
	}
	
	public void setSkillMetadata(SkillMetadata data) {
		if(data!=null) this.data.meta=data;
	}
	
	public void setMythicMeta(dMythicMeta data) {
		this.data=data;
	}
	
	public dMythicMeta getMythicMeta() {
		return this.data;
	}
	
	public SkillMetadata getSkillMetadata() {
		if(this.data!=null&&this.data.meta!=null) return this.data.meta;
		return null;
	}
	
	public boolean execute(SkillMetadata data) {
		if(data==null) data=this.data.meta;
		if(this.skill.isUsable(data)) {
			this.skill.execute(data);
			return true;
		}
		return false;
	}
	
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		int i1=1;
		if(a.startsWith("present")||a.startsWith("ispresent")) {
			return new ElementTag(isPresent()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("type")) {
			return new ElementTag(isPresent()?skill.getInternalName():null).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("data")) {
			if(this.data!=null) return this.data.getAttribute(a.fulfill(i1));
			return null;
		} else if(a.startsWith("isuseable")||a.startsWith("checkconditions")) {
			SkillMetadata data=null;
			if(this.data!=null) data=this.data.meta;
			if(a.hasContext(i1)&&dMythicMeta.matches(a.getContext(i1))) {
				data=((dMythicMeta)a.getContextObject(i1)).meta;
			}
			return new ElementTag(skill.isUsable(data)).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("execute")) {
			SkillMetadata data=null;
			if(this.data!=null) data=this.data.meta;
			if(a.hasContext(i1)&&dMythicMeta.matches(a.getContext(i1))) {
				data=((dMythicMeta)a.getContextObject(i1)).meta;
			}
			boolean bl1=skill.isUsable(data);
			if(bl1) skill.execute(data);
			return new ElementTag(bl1).getAttribute(a.fulfill(i1));
		}
		return new ElementTag(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
		ElementTag element=m.getValue();
		switch (m.getName().toLowerCase()) {
			case "data":
				if (element.matchesType(dMythicMeta.class)) this.data=element.asType(dMythicMeta.class);
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
		String s1=this.metaId.isPresent()?this.metaId.get():"";
		return this.skill!=null?id+this.skill.getInternalName()+s1:null;
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
		this.prefix = string;
		return this;
	}
	
	@Override
	public String toString() {
		return this.identify();
	}
	
    @Fetchable("mythicskill")
    public static dMythicSkill valueOf(String name,TagContext context) {
        if (name==null||name.isEmpty()) return null;
        String metaId=null;
        try {
        	if (name.contains(dMythicMeta.id)) {
        		String[]arr1=name.split(dMythicMeta.id);
        		name=arr1[0];
        		metaId=arr1[1];
        	}
        	name=name.replace(id,"");
            return new dMythicSkill(name,metaId);
        }
        catch (Exception e) {
        	Debug.echoError(e.getMessage());
        }
        return null;
    }

	@Override
	public void applyProperty(Mechanism arg0) {
	}

}
