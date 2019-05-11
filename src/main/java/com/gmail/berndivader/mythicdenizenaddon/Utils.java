package com.gmail.berndivader.mythicdenizenaddon;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;

public 
class 
Utils 
{
	
	public static <E extends Enum<E>> E enum_lookup(Class<E> e, String id) {
		E result;
		try {
			result=Enum.valueOf(e,id);
		} catch (IllegalArgumentException e1) {
			result=null;
		}
		return result;
	}
	
	public static AbstractMap.SimpleEntry<HashSet<AbstractEntity>,HashSet<AbstractLocation>> split_target_list(dList list) {
		HashSet<AbstractLocation>location_targets=new HashSet<>();
		HashSet<AbstractEntity>entity_targets=new HashSet<>();
		int size=list.size();
		for(int i1=0;i1<size;i1++) {
			Element e=(Element)list.getObject(i1);
			if(e.matchesType(dLocation.class)) {
				location_targets.add(BukkitAdapter.adapt((Location)e.asType(dLocation.class)));
			} else if (e.matchesType(dEntity.class)) {
				entity_targets.add(BukkitAdapter.adapt(((dEntity)e.asType(dEntity.class)).getBukkitEntity()));
			}
		}
		return new AbstractMap.SimpleEntry<HashSet<AbstractEntity>, HashSet<AbstractLocation>>(entity_targets,location_targets);
	}
	
	public static HashMap<String,String> parse_attributes(String line) {
		if(line.contains("{")&&line.contains("}")) {
			String parse[]=line.split("\\{")[1].split("\\}")[0].split(";");
			HashMap<String,String>attributes=new HashMap<>();
			int size=parse.length;
			for(int i1=0;i1<size;i1++) {
				if(parse[i1].startsWith("script")) continue;
				String arr1[]=parse[i1].split("=");
				if(arr1.length==2) attributes.put(arr1[0],arr1[1]);
			}
			return attributes;
		}
		return null;
	}

}
