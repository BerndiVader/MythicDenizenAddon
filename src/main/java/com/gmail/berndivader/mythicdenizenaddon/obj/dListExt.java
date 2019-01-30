package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;

import com.gmail.berndivader.mythicdenizenaddon.QuickSort;
import com.gmail.berndivader.mythicdenizenaddon.QuickSortPair;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dListExt extends dObjectExtension {
	private dList list;
	static Random random=new Random();
	
	public dListExt (dList l) {
		this.list=l;
	}
	
	public static boolean describes(dObject object) {
        return object instanceof dList;
    }
	
    public static dListExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dListExt((dList)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	if (a==null||a.attributes.length==0) return null;
   		String s1=a.getAttribute(1).toLowerCase();
   		if(s1.startsWith("sort_by_distance")&&a.hasContext(1)) {
   			String s3=a.getContext(1);
   			Location l1=dEntity.matches(s3)?new Element(s3).asType(dEntity.class).getLocation().clone():dLocation.matches(s3)?new Element(s3).asType(dLocation.class).clone():null;
   	    	return sort(list.identify(),l1).getAttribute(a.fulfill(1));
   		}
    	return null;
    }
    
    @Override
	public void adjust(Mechanism m) {
	}
    
    static dList sort(String s1,Location l1) {
		String[]arr1=s1.substring(3).split("\\|");
		QuickSortPair[]arr=new QuickSortPair[arr1.length];
		for(int i1=0;i1<arr1.length;i1++) {
			String s3=arr1[i1];
			double d1=l1.distance(dEntity.matches(s3)?new Element(s3).asType(dEntity.class).getLocation():dLocation.matches(s3)?new Element(s3).asType(dLocation.class).clone():l1);
			QuickSortPair pair=new QuickSortPair(d1,s3);
			arr[i1]=pair;
		}
		arr=QuickSort.sort(arr,0,arr.length-1);
		for(int i1=0;i1<arr.length;i1++) {
			arr1[i1]=(String)arr[i1].object;
		}
    	return new dList(Arrays.asList(arr1));
    }
    
}
