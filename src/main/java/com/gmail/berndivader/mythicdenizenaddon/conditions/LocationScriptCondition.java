package com.gmail.berndivader.mythicdenizenaddon.conditions;

import com.denizenscript.denizen.objects.LocationTag;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public
class 
LocationScriptCondition 
extends
ScriptCondition<LocationTag>
implements
ILocationCondition
{
	public LocationScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractLocation location) {
		return __check(new LocationTag(BukkitAdapter.adapt(location)));
	}

}
