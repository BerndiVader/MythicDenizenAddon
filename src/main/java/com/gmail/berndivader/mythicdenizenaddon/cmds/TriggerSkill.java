package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.apache.commons.lang3.tuple.Pair;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.gmail.berndivader.mythicdenizenaddon.Statics;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

public
class 
TriggerSkill 
extends 
AbstractCommand 
{

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (Argument arg:entry.getProcessedArgs()) {
			if (!entry.hasObject(Statics.str_activemob) && arg.matchesPrefix(Statics.str_activemob) && arg.matchesArgumentType(dActiveMob.class))  {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(dActiveMob.class));
			} else if (!entry.hasObject(Statics.str_trigger) && arg.matchesPrefix(Statics.str_trigger)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asElement());
			} else if (!entry.hasObject(Statics.str_entity) && arg.matchesPrefix(Statics.str_entity) && arg.matchesArgumentType(EntityTag.class)) {
				entry.addObject(arg.getPrefix().getValue(), arg.asType(EntityTag.class));
			}
		}
	}
	
	@Override
	public void execute(ScriptEntry entry) {
		if (!entry.hasObject(Statics.str_activemob) || !entry.hasObject(Statics.str_entity) || !entry.hasObject(Statics.str_trigger)) return;
		SkillTrigger trigger = SkillTrigger.API;
		try {
			trigger = SkillTrigger.valueOf(entry.getElement(Statics.str_trigger).asString().toUpperCase());
		} catch (Exception ex) {
			Debug.echoError(ex.getMessage());
			return;
		}
		ActiveMob am = ((dActiveMob)entry.getObject(Statics.str_activemob)).getActiveMob();
		AbstractEntity ae = BukkitAdapter.adapt(((EntityTag)entry.getObject(Statics.str_entity)).getBukkitEntity());
		new TriggeredSkill(trigger,am,ae,new Pair[0]);
	}
}
