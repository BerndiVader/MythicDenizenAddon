package com.gmail.berndivader.mmDenizenAddon.plugins.cmds;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dActiveMob;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class TransformToMythicMob extends AbstractCommand {
	public enum Types {
		entity,
		mobtype,
		level,
		activemob
	}

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg : aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.entity.name()) && arg.matchesPrefix(Types.entity.name()) 
					&& arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.entity.name(), arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.mobtype.name()) && arg.matchesPrefix(Types.mobtype.name())) {
				entry.addObject(Types.mobtype.name(), arg.asElement());
			} else if (!entry.hasObject(Types.level.name()) && arg.matchesPrefix(Types.level.name())) {
				entry.addObject(Types.level.name(), arg.asElement());
			}
		}
		if (!entry.hasObject(Types.level.name())) entry.addObject(Types.level.name(), 1);
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Entity entity = ((dEntity)entry.getdObject(Types.entity.name())).getBukkitEntity();
		String mmName = entry.getElement(Types.mobtype.name()).asString();
		MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(mmName);
		int level = entry.getElement(Types.level.name()).asInt();
		if (mm==null 
				|| !(entity instanceof LivingEntity) 
				|| (entity instanceof Player && !mm.isPersistent())) return;
		ActiveMob am = TransformToMythicMob.transformEntityToMythicMob(entity, mm, level);
		if (am!=null) {
			entry.addObject(Types.activemob.name(), new dActiveMob(am).identify());
		} else {
			entry.addObject(Types.activemob.name(), Types.activemob.name()+"@");
		}
	}
	
	private static ActiveMob transformEntityToMythicMob(Entity l, MythicMob mm, int level) {
		ActiveMob am = new ActiveMob(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, level);
	    TransformToMythicMob.addActiveMobToFaction(mm, am);
	    TransformToMythicMob.registerActiveMob(am);
	    return am;
	}
	
	public static void addActiveMobToFaction(MythicMob mm, ActiveMob am) {
        if (mm.hasFaction()) {
            am.setFaction(mm.getFaction());
            am.getLivingEntity().setMetadata("Faction", new FixedMetadataValue(MythicMobs.inst(),mm.getFaction()));
        }
	}	
	
    public static void registerActiveMob(ActiveMob am) {
        MythicMobs.inst().getMobManager().registerActiveMob(am);
    }

}
