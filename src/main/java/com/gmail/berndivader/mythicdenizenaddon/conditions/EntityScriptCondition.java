package com.gmail.berndivader.mythicdenizenaddon.conditions;

import com.denizenscript.denizen.objects.EntityTag;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public
class 
EntityScriptCondition 
extends
ScriptCondition<EntityTag>
implements
IEntityCondition
{
	public EntityScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractEntity abstract_entity) {
		return __check(new EntityTag(abstract_entity.getBukkitEntity()));
	}

}
