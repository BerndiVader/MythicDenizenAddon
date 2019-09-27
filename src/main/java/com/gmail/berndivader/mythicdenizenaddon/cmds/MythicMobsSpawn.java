package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;

public class MythicMobsSpawn extends AbstractCommand {

    static MobManager mobmanager = MythicMobsAddon.mythicMobsInstance.getMobManager();

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.MOB_TYPE) && arg.matchesPrefix(StaticStrings.MOB_TYPE)) {
                entry.addObject(StaticStrings.MOB_TYPE, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.LOCATION) && arg.matchesArgumentType(LocationTag.class)) {
                entry.addObject(StaticStrings.LOCATION, arg.asType(LocationTag.class));
            } else if (!entry.hasObject(StaticStrings.LEVEL) && arg.matchesPrefix(StaticStrings.LEVEL)
                    && arg.matchesPrimitive(PrimitiveType.Integer)) {
                entry.addObject(StaticStrings.LEVEL, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.WORLD) && arg.matchesPrefix(StaticStrings.WORLD)) {
                entry.addObject(StaticStrings.WORLD, arg.asElement());
            }
        }

        if (!entry.hasObject(StaticStrings.MOB_TYPE) || !entry.hasObject(StaticStrings.LOCATION)) {
            throw new InvalidArgumentsException(StaticStrings.MOB_TYPE + " & " + StaticStrings.LOCATION + " is required!");
        }

        entry.addObject(StaticStrings.LEVEL, new ElementTag(1));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        String mobtype = entry.getElement(StaticStrings.MOB_TYPE).asString();
        if (mobmanager.getMythicMob(mobtype) == null) {
            throw new CommandExecutionException("No MythicMobType found!");
        }

        int level = entry.getElement(StaticStrings.LEVEL).asInt();
        LocationTag loc = entry.getObjectTag(StaticStrings.LOCATION);
        AbstractLocation location = BukkitAdapter.adapt(loc);
        if (location == null || loc.getWorld() == null) {
            return;
        }
        AbstractWorld world = BukkitAdapter.adapt(loc.getWorld());
        AbstractLocation sl = new AbstractLocation(world, location.getX(), location.getY(), location.getZ());
        ActiveMob am = mobmanager.spawnMob(mobtype, sl, level);

        if (am == null) {
            throw new CommandExecutionException("Failed to spawn MythicMob");
        }
        entry.addObject("activemob", new dActiveMob(am));
    }
}
