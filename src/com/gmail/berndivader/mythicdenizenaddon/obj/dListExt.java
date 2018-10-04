package com.gmail.berndivader.mythicdenizenaddon.obj;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;

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
    	if (a==null||a.attributes.length<1) return null;
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
		Double[]arr2=new Double[0];
		for(String s3:arr1) {
			double d1=l1.distance(dEntity.matches(s3)?new Element(s3).asType(dEntity.class).getLocation():dLocation.matches(s3)?new Element(s3).asType(dLocation.class).clone():l1);
			arr2=add(arr2,d1);
		}
    	return new dList(Arrays.asList(bSort(arr2,arr1,arr2.length)));
    }
    
    static String[] bSort(Double[]dist1,String[]arr1,int n1) {
    	for (int i1=0;i1<n1;i1++) {
    		for (int j1=1;j1<(n1-i1);j1++) {
    			if (dist1[j1-1]>dist1[j1]) {
    				arr1=(String[])swp(j1,j1-1,arr1);
    				dist1=(Double[])swp(j1,j1-1,dist1);
    			}
    		}
    	}
    	return arr1;
    }
    
    static Object[] swp(int i1,int j1,Object[]arr1) {
    	Object s1=arr1[i1];
    	arr1[i1]=arr1[j1];
    	arr1[j1]=s1;
    	return arr1;
    }
    
    static Double[] add(Double[] arr1,double d2) {
    	int i1=arr1.length;
		Double[]arr2=new Double[]{d2};
		Double[]arr=new Double[i1+1];
		System.arraycopy(arr1,0,arr,0,i1);
		System.arraycopy(arr2,0,arr,i1,1);
		return arr;
    }
    
}
