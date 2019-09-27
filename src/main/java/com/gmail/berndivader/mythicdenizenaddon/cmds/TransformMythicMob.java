package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class TransformMythicMob extends AbstractCommand {

    private Entity transformToNormalEntity(ActiveMob am) {
        Entity entity = am.getEntity().getBukkitEntity();
        entity.removeMetadata("Faction", MythicMobsAddon.mythicMobsInstance);
        Location l = am.getEntity().getBukkitEntity().getLocation().clone();

        am.setDead();
        unregister(am.getUniqueId());

        l.setY(0);
        AbstractEntity fakeActiveMob = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
        am.setEntity(fakeActiveMob);
        unregister(am);

        MythicMobsAddon.mythicMobManager.getVoidList().remove(entity.getUniqueId());
        fakeActiveMob.remove();
        return entity;
    }

    private void unregister(ActiveMob activeMob) {
        if (activeMob != null) {
            MythicMobsAddon.mythicMobManager.unregisterActiveMob(activeMob);
        }
    }

    private void unregister(UUID uuid) {
        if (uuid != null) {
            MythicMobsAddon.mythicMobManager.unregisterActiveMob(uuid);
        }
    }

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.ACTIVE_MOB) && arg.matchesPrefix(StaticStrings.ACTIVE_MOB)
                    && arg.matchesArgumentType(dActiveMob.class)) {
                entry.addObject(StaticStrings.ACTIVE_MOB, arg.asType(dActiveMob.class));
            }
        }
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        ActiveMob am = ((dActiveMob) entry.getObjectTag(StaticStrings.ACTIVE_MOB)).getActiveMob();
        if (am != null) {
            Entity entity = transformToNormalEntity(am);
            EntityTag dentity = new EntityTag(entity);
            entry.addObject(StaticStrings.ENTITY, dentity);
        }
    }
}
