package com.gmail.berndivader.mythicdenizenaddon.obj;

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
    	if (a==null) return null;
    	if (a.attributes.size()>0) {
    		String s1=a.getAttribute(1).toLowerCase();
    		if(s1.startsWith("sort_by_distance")&&a.hasContext(1)) {
    			String s3=a.getContext(1);
    			Location l1=dEntity.matches(s3)?new Element(s3).asType(dEntity.class).getLocation().clone():dLocation.matches(s3)?new Element(s3).asType(dLocation.class).clone():null;
    			String s4=sort(list.identify(),l1);
    	    	return new Element(s4).getAttribute(a.fulfill(1));
    		}
    	}
    	return null;
    }
    
    @Override
	public void adjust(Mechanism m) {
	}
    
    static String sort(String s1,Location l1) {
		String[]arr1=s1.substring(3).split("\\|");
		String s2=new String("");
		for(String s3:arr1) {
			double d1=l1.distance(dEntity.matches(s3)?new Element(s3).asType(dEntity.class).getLocation():dLocation.matches(s3)?new Element(s3).asType(dLocation.class).clone():l1);
			s2+=d1+"|";
		}
    	s2=s2.substring(0,s2.length()-1);
    	String[]dist1=s2.split("\\|");
    	arr1=bSort(dist1,arr1,arr1.length);
    	s1="li@";
    	for(String temp:arr1) {
    		s1+=temp+"|";
    	}
    	return s1.substring(0,s1.length()-1);
    }
    
    static String[] bSort(String[]dist1,String[]arr1,int n1) {
    	for (int i1=0;i1<n1;i1++) {
    		for (int j1=1;j1<(n1-i1);j1++) {
    			if (Double.parseDouble(dist1[j1-1])>Double.parseDouble(dist1[j1])) {
    				swp(j1,j1-1,arr1);
    				swp(j1,j1-1,dist1);
    			}
    		}
    	}    	
    	return arr1;
    }
    
    static String[] swp(int i1,int j1,String[]arr1) {
    	String s1=arr1[i1];
    	arr1[i1]=arr1[j1];
    	arr1[j1]=s1;
    	return arr1;
    }
    
}
