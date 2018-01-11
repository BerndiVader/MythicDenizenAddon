package com.gmail.berndivader.mythicdenizenaddon.cmds;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicdenizenaddon.Types;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;

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

	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if (!entry.hasObject(Types.entity.a())&&arg.matchesPrefix(Types.entity.a())&&arg.matchesArgumentType(dEntity.class)) {
				entry.addObject(Types.entity.a(),arg.asType(dEntity.class));
			} else if (!entry.hasObject(Types.mobtype.a()) && arg.matchesPrefix(Types.mobtype.a())) {
				entry.addObject(Types.mobtype.a(), arg.asElement());
			} else if (!entry.hasObject(Types.level.a()) && arg.matchesPrefix(Types.level.a())) {
				entry.addObject(Types.level.a(), arg.asElement());
			}
		}
		if (!entry.hasObject(Types.level.a())) entry.addObject(Types.level.a(), 1);
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Entity entity = ((dEntity)entry.getdObject(Types.entity.a())).getBukkitEntity();
		String mmName = entry.getElement(Types.mobtype.a()).asString();
		MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(mmName);
		int level = entry.getElement(Types.level.a()).asInt();
		ActiveMob am=null;
		if (mm!=null) am=TransformToMythicMob.transformEntityToMythicMob(entity,mm,level);
		entry.addObject(Types.activemob.a(),am!=null?new dActiveMob(am):Types.activemob.a()+"@");
	}
	
	private static ActiveMob transformEntityToMythicMob(Entity l, MythicMob mm, int level) {
		ActiveMob am = new ActiveMob(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, level);
	    TransformToMythicMob.addActiveMobToFaction(mm,am);
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
