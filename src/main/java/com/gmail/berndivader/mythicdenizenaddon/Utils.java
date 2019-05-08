package com.gmail.berndivader.mythicdenizenaddon;

import java.util.AbstractMap;
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

}
