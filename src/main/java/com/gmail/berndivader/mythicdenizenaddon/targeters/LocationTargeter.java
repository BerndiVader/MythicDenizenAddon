package com.gmail.berndivader.mythicdenizenaddon.targeters;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.gmail.berndivader.mythicdenizenaddon.Utils;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

import java.util.HashMap;
import java.util.HashSet;

public class LocationTargeter extends ILocationSelector {

    final String scriptName;
    HashMap<String, String> attributes;

    public LocationTargeter(String targeter, MythicLineConfig mlc) {
        super(mlc);

        scriptName = mlc.getString("script", "");
        if (targeter.contains("{") && targeter.contains("}")) {
            String[] parse = targeter.split("\\{")[1].split("}")[0].split(";");
            attributes = new HashMap<>();

            for (String s : parse) {
                if (s.startsWith("script")) {
                    continue;
                }
                String[] arr = s.split("=");
                if (arr.length == 2) {
                    attributes.put(arr[0], arr[1]);
                }
            }
        }
    }

    @Override
    public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
        HashSet<AbstractLocation> locations = new HashSet<>();
        ListTag targets = Utils.getTargetsForScriptTargeter(data, scriptName, attributes);
        if (targets != null) {
            for (int i = 0; i < targets.size(); i++) {
                ElementTag element = (ElementTag) targets.getObject(i);
                if (element.matchesType(LocationTag.class)) {
                    locations.add(BukkitAdapter.adapt(element.asType(LocationTag.class)));
                }
            }
        }
        return locations;
    }
}
