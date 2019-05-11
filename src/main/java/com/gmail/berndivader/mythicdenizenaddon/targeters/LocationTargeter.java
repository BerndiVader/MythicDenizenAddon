package com.gmail.berndivader.mythicdenizenaddon.targeters;

import java.util.HashMap;
import java.util.HashSet;

import com.gmail.berndivader.mythicdenizenaddon.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;

public 
class 
LocationTargeter
extends
ILocationSelector
{
	final String script_name;
	HashMap<String,String>attributes;

	public LocationTargeter(String targeter,MythicLineConfig mlc) {
		super();
		
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
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation>locations=new HashSet<AbstractLocation>();
		dList targets=Utils.getTargetsForScriptTargeter(data,script_name,attributes);
		if(targets!=null) {
			for(int i1=0;i1<targets.size();i1++) {
				Element e=(Element)targets.getObject(i1);
				if(e.matchesType(dLocation.class)) {
					locations.add(BukkitAdapter.adapt(e.asType(dLocation.class)));
				}
			}
		}
		return locations;
	}

}
