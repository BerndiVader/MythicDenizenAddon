package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

public class TransformToMythicMob extends AbstractCommand {

    private ActiveMob transformEntityToMythicMob(Entity entity, MythicMob mythicMob, int level) {
        ActiveMob am = new ActiveMob(entity.getUniqueId(), BukkitAdapter.adapt(entity), mythicMob, level);
        addActiveMobToFaction(mythicMob, am);
        registerActiveMob(am);
        return am;
    }

    public void addActiveMobToFaction(MythicMob mythicMob, ActiveMob activeMob) {
        if (mythicMob.hasFaction()) {
            activeMob.setFaction(mythicMob.getFaction());
            activeMob.getLivingEntity().setMetadata("Faction", new FixedMetadataValue(MythicMobsAddon.mythicMobsInstance, mythicMob.getFaction()));
        }
    }

    public void registerActiveMob(ActiveMob activeMob) {
        MythicMobsAddon.mythicMobManager.registerActiveMob(activeMob);
    }

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.ENTITY) && arg.matchesPrefix(StaticStrings.ENTITY) && arg.matchesArgumentType(EntityTag.class)) {
                entry.addObject(StaticStrings.ENTITY, arg.asType(EntityTag.class));
            } else if (!entry.hasObject(StaticStrings.MOB_TYPE) && arg.matchesPrefix(StaticStrings.MOB_TYPE)) {
                entry.addObject(StaticStrings.MOB_TYPE, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.LEVEL) && arg.matchesPrefix(StaticStrings.LEVEL)) {
                entry.addObject(StaticStrings.LEVEL, arg.asElement());
            }
        }

        entry.defaultObject(StaticStrings.LEVEL, new ElementTag(1));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        Entity entity = ((EntityTag) entry.getObjectTag(StaticStrings.ENTITY)).getBukkitEntity();
        String mmName = entry.getElement(StaticStrings.MOB_TYPE).asString();
        MythicMob mm = MythicMobsAddon.mythicMobsInstance.getMobManager().getMythicMob(mmName);
        int level = entry.getElement(StaticStrings.LEVEL).asInt();

        ActiveMob am = null;
        if (mm != null) {
            am = transformEntityToMythicMob(entity, mm, level);
        }
        if (am != null) {
            entry.addObject(StaticStrings.ACTIVE_MOB, new dActiveMob(am));
        } else {
            throw new CommandExecutionException("Failed to transform Entity to MythicMobs!");
        }
    }
}
