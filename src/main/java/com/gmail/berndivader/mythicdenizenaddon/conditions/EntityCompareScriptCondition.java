package com.gmail.berndivader.mythicdenizenaddon.conditions;

import com.denizenscript.denizen.objects.EntityTag;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

public
class 
EntityCompareScriptCondition 
extends
ScriptCondition<EntityTag>
implements
IEntityComparisonCondition
{
	public EntityCompareScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		return __check(new EntityTag(source.getBukkitEntity()),new EntityTag(target.getBukkitEntity()));
	}

}
