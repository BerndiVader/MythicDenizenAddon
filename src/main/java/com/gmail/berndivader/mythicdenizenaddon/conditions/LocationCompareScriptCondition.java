package com.gmail.berndivader.mythicdenizenaddon.conditions;

import com.denizenscript.denizen.objects.LocationTag;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;

public class LocationCompareScriptCondition extends ScriptCondition<LocationTag> implements ILocationComparisonCondition {

    public LocationCompareScriptCondition(String line, MythicLineConfig mlc) {
        super(line, mlc);
    }

    @Override
    public boolean check(AbstractLocation source, AbstractLocation target) {
        return check(new LocationTag(BukkitAdapter.adapt(source)), new LocationTag(BukkitAdapter.adapt(target)));
    }
}
