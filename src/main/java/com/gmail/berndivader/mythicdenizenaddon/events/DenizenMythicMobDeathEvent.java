package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizen.utilities.DenizenAPI;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DenizenMythicMobDeathEvent extends BukkitScriptEvent implements Listener {

    public static DenizenMythicMobDeathEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        String s1 = s.toLowerCase();
        return s1.startsWith("mm denizen death") || s1.startsWith("mythicmobs death");
    }

    @Override
    public boolean matches(ScriptContainer container, String s) {
        return true;
    }

    public MythicMobDeathEvent event;

    public DenizenMythicMobDeathEvent() {
        instance = this;
    }

    @Override
    public String getName() {
        return "MythicMobsDeathEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        MythicMobDeathEvent.getHandlerList().unregister(this);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        EntityTag killer = new EntityTag(event.getEntity());
        return new BukkitScriptEntryData(killer.isPlayer() ? killer.getDenizenPlayer() : null, killer.isNPC() ? killer.getDenizenNPC() : null);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        boolean modified = false;
        for (String part : tag.toString().split(";")) {
            String[] parse = part.split(":");
            String prefix = parse[0].toLowerCase();
            String value = parse[1];

            switch (prefix) {
                case "drops":
                    if (Argument.valueOf(value).matchesArgumentType(ListTag.class)) {
                        List<ItemStack> is = new ArrayList<>();
                        for (ItemTag di : Argument.valueOf(value).asType(ListTag.class).filter(ItemTag.class)) {
                            is.add(di.getItemStack());
                        }
                        event.setDrops(is);
                        modified = true;
                    }
                    break;
                case "money":
                    if (Argument.valueOf(value).matchesPrimitive(PrimitiveType.Double)) {
                        event.setCurrency(Double.parseDouble(value));
                        modified = true;
                    }
                    break;
                case "exp":
                case "xp":
                    if (Argument.valueOf(value).matchesPrimitive(PrimitiveType.Integer)) {
                        event.setExp(Integer.parseInt(value));
                        modified = true;
                    }
                    break;
            }
        }
        return modified || super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "drops":
                ListTag dl = new ListTag();
                for (ItemStack i : event.getDrops()) {
                    dl.add(new ItemTag(i).identify());
                }
                return dl;
            case "killer":
            case "attacker":
                return new EntityTag(event.getKiller());
            case "victim":
            case "entity":
                return new EntityTag(event.getEntity());
            case "activemob":
                return new dActiveMob(event.getMob());
            case "money":
                return new ElementTag(event.getCurrency());
            case "exp":
                return new ElementTag(event.getExp());
            case "event":
                return new ElementTag(this.event.toString());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobsDeathEvent(MythicMobDeathEvent event) {
        this.event = event;
        fire();
    }
}
