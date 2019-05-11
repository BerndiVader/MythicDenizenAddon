package com.gmail.berndivader.mythicdenizenaddon.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import net.aufdemrand.denizen.objects.dEntity;

public
class 
EntityCompareScriptCondition 
extends
ScriptCondition<dEntity>
implements
IEntityComparisonCondition
{
	public EntityCompareScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		return __check(new dEntity(source.getBukkitEntity()),new dEntity(target.getBukkitEntity()));
	}

}
