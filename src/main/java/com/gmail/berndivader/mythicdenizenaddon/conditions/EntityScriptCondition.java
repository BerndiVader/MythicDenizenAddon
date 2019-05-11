package com.gmail.berndivader.mythicdenizenaddon.conditions;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import net.aufdemrand.denizen.objects.dEntity;

public
class 
EntityScriptCondition 
extends
ScriptCondition<dEntity>
implements
IEntityCondition
{
	public EntityScriptCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
	}

	@Override
	public boolean check(AbstractEntity abstract_entity) {
		System.err.println(abstract_entity.getUniqueId());
		return __check(new dEntity(abstract_entity.getBukkitEntity()));
	}

}
