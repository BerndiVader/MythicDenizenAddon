package com.gmail.berndivader.mythicdenizenaddon.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationComparisonCondition;
import net.aufdemrand.denizen.objects.dLocation;

public
class 
LocationCompareScriptCondition 
extends
ScriptCondition<dLocation>
implements
ILocationComparisonCondition
{
	public LocationCompareScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractLocation source, AbstractLocation target) {
		return __check(new dLocation(BukkitAdapter.adapt(source)),new dLocation(BukkitAdapter.adapt(target)));
	}

}
