package com.gmail.berndivader.mythicdenizenaddon.targeters;

import java.util.HashMap;
import java.util.HashSet;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.gmail.berndivader.mythicdenizenaddon.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.IEntitySelector;

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
		ListTag targets=Utils.getTargetsForScriptTargeter(data,script_name,attributes);
		if(targets!=null) {
			for(int i1=0;i1<targets.size();i1++) {
				ElementTag e=(ElementTag)targets.getObject(i1);
				if(e.matchesType(EntityTag.class)) {
					entities.add(BukkitAdapter.adapt(e.asType(EntityTag.class,null).getBukkitEntity()));
				}
			}
		}
		return entities;
	}

}
