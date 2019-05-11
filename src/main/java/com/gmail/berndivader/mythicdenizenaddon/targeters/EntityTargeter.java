package com.gmail.berndivader.mythicdenizenaddon.targeters;

import java.util.HashMap;
import java.util.HashSet;

import com.gmail.berndivader.mythicdenizenaddon.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;

public 
class 
EntityTargeter
extends
IEntitySelector
{
	final String script_name;
	HashMap<String,String>attributes;

	public EntityTargeter(String targeter,MythicLineConfig mlc) {
		super(mlc);
		
		script_name=mlc.getString("script","");

		if(targeter.contains("{")&&targeter.contains("}")) {
			String parse[]=targeter.split("\\{")[1].split("\\}")[0].split(";");
			attributes=new HashMap<>();
			int size=parse.length;
			for(int i1=0;i1<size;i1++) {
				if(parse[i1].startsWith("script")) continue;
				String arr1[]=parse[i1].split("=");
				if(arr1.length==2) attributes.put(arr1[0],arr1[1]);
			}
		}
	}

	@Override
	public HashSet<AbstractEntity> getEntities(SkillMetadata data) {
		HashSet<AbstractEntity>entities=new HashSet<AbstractEntity>();
		dList targets=Utils.getTargetsForScriptTargeter(data,script_name,attributes);
		if(targets!=null) {
			for(int i1=0;i1<targets.size();i1++) {
				Element e=(Element)targets.getObject(i1);
				if(e.matchesType(dEntity.class)) {
					entities.add(BukkitAdapter.adapt(e.asType(dEntity.class).getBukkitEntity()));
				}
			}
		}
		return entities;
	}

}
