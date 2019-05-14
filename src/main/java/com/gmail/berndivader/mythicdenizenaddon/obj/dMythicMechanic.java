package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.Optional;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillManager;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public 
class 
dMythicMechanic 
implements
dObject,
Adjustable
{
	
	public static String id="mythicmechanic@";
	static MythicMobs mythicmobs;
	static SkillManager skillmanager;
	
	private String prefix,name;
	SkillMechanic mechanic;
	
	dMythicMeta data;
	Optional<String> metaId;
	
	static {
		mythicmobs=MythicMobs.inst();
		skillmanager=mythicmobs.getSkillManager();
	}

	public dMythicMechanic(String name) {
		this(name,null,"");
	}
	
	public dMythicMechanic(String name,String line) {
		this(name,null,line);
	}
	
	public dMythicMechanic(String name,String meta_hash,String line) {
		this.name=name;
		
		if((mechanic=skillmanager.getSkillMechanic(parseLine(line)))==null) dB.echoError("MythicMechanic "+name+" not present!");
		if (((this.metaId=Optional.ofNullable(meta_hash)).isPresent())&&dMythicMeta.objects.containsKey(this.metaId.get())) {
			data=new dMythicMeta(dMythicMeta.objects.get(this.metaId.get()));
		}
	}
	
    public static boolean matches(String string) {
        return valueOf(string)!=null;
    }
    
	public static dMythicMechanic valueOf(String name) {
		return valueOf(name,null);
	}
	
	public String parseLine(String line) {
		if(line==null) line="";
		if(!line.startsWith("{")) line="{"+line+"}";
		return MythicLineConfig.unparseBlock(name+line);
	}
	
	public boolean isPresent() {
		return this.mechanic!=null;
	}
	
	public boolean hasMeta() {
		return this.data!=null&&this.data.meta!=null;
	}
	
	public void setSkillMetadata(SkillMetadata data) {
		if(data!=null) this.data.meta=data;
	}
	public SkillMetadata getSkillMetadata() {
		if(this.data!=null&&this.data.meta!=null) return this.data.meta;
		return null;
	}
	
	
	public void setMythicMeta(dMythicMeta data) {
		this.data=data;
	}
	public dMythicMeta getMythicMeta() {
		return this.data;
	}
	
	public boolean execute(SkillMetadata data) {
		if(data==null) data=this.data.meta;
		return false;
	}
	
	public void init(String line) {
		if(mechanic!=null) mechanic.init(line,new MythicLineConfig(line));
	}
	
	public String getAttribute(Attribute a) {
		if(a==null) return null;
		int i1=1;
		if(a.startsWith("present")||a.startsWith("ispresent")) {
			return new Element(isPresent()).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("type")) {
			return new Element(isPresent()?name:null).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("data")) {
			if(this.data!=null) return this.data.getAttribute(a.fulfill(i1));
			return null;
		} else if(a.startsWith("is_useable")||a.startsWith("isuseable")) {
			SkillMetadata data=null;
			if(this.data!=null) data=this.data.meta;
			if(a.hasContext(i1)&&dMythicMeta.matches(a.getContext(i1))) {
				data=((dMythicMeta)a.getContextObject(i1)).meta;
			}
			return new Element(mechanic.usable(data)).getAttribute(a.fulfill(i1));
		} else if(a.startsWith("execute")) {
			SkillMetadata data=null;
			if(this.data!=null) data=this.data.meta;
			if(a.hasContext(i1)&&dMythicMeta.matches(a.getContext(i1))) {
				data=((dMythicMeta)a.getContextObject(i1)).meta;
			}
			return new Element(mechanic.execute(data)).getAttribute(a.fulfill(i1));
		}
		return new Element(identify()).getAttribute(a);
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element element=m.getValue();
		switch (m.getName().toLowerCase()) {
			case "data":
				if (element.matchesType(dMythicMeta.class)) this.data=element.asType(dMythicMeta.class);
				break;
			case "init":
				if(element.isString()) init(element.asString());
				break;
		}
	}
	
	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "MythicMechanic";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		String s1=this.metaId.isPresent()?this.metaId.get():"";
		return this.mechanic!=null?id+this.name+s1:null;
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
	
    @Fetchable("mythicmechanic")
    public static dMythicMechanic valueOf(String name,TagContext context) {
        if (name==null||name.isEmpty()) return null;
        String metaId=null;
        try {
        	if (name.contains(dMythicMeta.id)) {
        		String[]arr1=name.split(dMythicMeta.id);
        		name=arr1[0];
        		metaId=arr1[1];
        	}
        	name=name.replace(id,"");
            return new dMythicMechanic(name,metaId);
        }
        catch (Exception e) {
        	dB.echoError(e.getMessage());
        }
        return null;
    }

	@Override
	public void applyProperty(Mechanism arg0) {
	}
}
