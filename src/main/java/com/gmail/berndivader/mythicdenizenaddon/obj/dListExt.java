package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.QuickSort;
import com.gmail.berndivader.mythicdenizenaddon.QuickSortPair;

public 
class 
dListExt
extends 
dObjectExtension 
{
	private ListTag list;
	static Random random=new Random();
	
	public dListExt (ListTag l) {
		this.list=l;
	}
	
	public static boolean describes(ObjectTag object) {
        return object instanceof ListTag;
    }
	
    public static dListExt getFrom(ObjectTag o) {
    	if (!describes(o)) return null;
    	return new dListExt((ListTag)o);
    }
    
    @Override
    public String getAttribute(Attribute a) {
    	if (a==null||a.attributes.length==0) return null;
   		String s1=a.getAttribute(1).toLowerCase();
   		if(s1.startsWith("sort_by_distance")&&a.hasContext(1)) {
   			String s3=a.getContext(1);
   			Location l1=EntityTag.matches(s3)?new ElementTag(s3).asType(EntityTag.class,a.context).getLocation().clone():LocationTag.matches(s3)?new ElementTag(s3).asType(LocationTag.class,a.context).clone():null;
   	    	return sort(list.identify(),l1).getAttribute(a.fulfill(1));
   		}
    	return null;
    }
    
    @Override
	public void adjust(Mechanism m) {
	}
    
    static ListTag sort(String s1,Location l1) {
		String[]arr1=s1.substring(3).split("\\|");
		QuickSortPair[]arr=new QuickSortPair[arr1.length];
		for(int i1=0;i1<arr1.length;i1++) {
			String s3=arr1[i1];
			double d1=l1.distance(EntityTag.matches(s3)?new ElementTag(s3).asType(EntityTag.class,null).getLocation():LocationTag.matches(s3)?new ElementTag(s3).asType(LocationTag.class,null).clone():l1);
			QuickSortPair pair=new QuickSortPair(d1,s3);
			arr[i1]=pair;
		}
		arr=QuickSort.sort(arr,0,arr.length-1);
		for(int i1=0;i1<arr.length;i1++) {
			arr1[i1]=(String)arr[i1].object;
		}
    	return new ListTag(Arrays.asList(arr1));
    }
    
}
