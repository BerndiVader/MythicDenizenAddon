package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.QuickSort;
import com.gmail.berndivader.mythicdenizenaddon.QuickSortPair;
import org.bukkit.Location;

import java.util.Arrays;

public class dListExt extends dObjectExtension {

    public static boolean describes(ObjectTag object) {
        return object instanceof ListTag;
    }

    public static dListExt getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        return new dListExt((ListTag) object);
    }

    private ListTag list;

    public dListExt(ListTag l) {
        this.list = l;
    }

    @Override
    public String getAttribute(Attribute a) {
        if (a == null || a.attributes.length == 0) {
            return null;
        }

        String s1 = a.getAttribute(1).toLowerCase();
        if (s1.startsWith("sort_by_distance") && a.hasContext(1)) {
            String s3 = a.getContext(1);
            Location l1 = EntityTag.matches(s3) ? new ElementTag(s3).asType(EntityTag.class).getLocation().clone() : LocationTag.matches(s3) ? new ElementTag(s3).asType(LocationTag.class).clone() : null;
            return sort(list.identify(), l1).getAttribute(a.fulfill(1));
        }

        return null;
    }

    static ListTag sort(String s1, Location l1) {
        String[] arr1 = s1.substring(3).split("\\|");
        QuickSortPair[] arr = new QuickSortPair[arr1.length];

        for (int i = 0; i < arr1.length; i++) {
            String s3 = arr1[i];
            double d1 = l1.distance(EntityTag.matches(s3) ? new ElementTag(s3).asType(EntityTag.class).getLocation() : LocationTag.matches(s3) ? new ElementTag(s3).asType(LocationTag.class).clone() : l1);
            QuickSortPair pair = new QuickSortPair(d1, s3);
            arr[i] = pair;
        }

        arr = QuickSort.sort(arr, 0, arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            arr1[i] = (String) arr[i].object;
        }

        return new ListTag(Arrays.asList(arr1));
    }
}
