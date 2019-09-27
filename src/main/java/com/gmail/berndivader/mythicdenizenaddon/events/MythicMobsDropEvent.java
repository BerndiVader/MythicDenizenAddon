package com.gmail.berndivader.mythicdenizenaddon.events;

import com.denizenscript.denizen.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper.PrimitiveType;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.denizencore.scripts.containers.ScriptContainer;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.dActiveMob;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitItemStack;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.*;
import io.lumine.xikage.mythicmobs.drops.droppables.ItemDrop;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MythicMobsDropEvent extends BukkitScriptEvent implements Listener {

    public static MythicMobsDropEvent instance;

    @Override
    public boolean couldMatch(ScriptContainer container, String s) {
        String s1 = s.toLowerCase();
        return s1.startsWith("mm lootdrop") || s1.startsWith("mythicmobs lootdrop");
    }

    @Override
    public boolean matches(ScriptContainer container, String s) {
        return true;
    }

    public MythicMobLootDropEvent event;
    private LootBag lootBag;

    public MythicMobsDropEvent() {
        instance = this;
    }

    private void setDrops(LootBag lootBag, ListTag dropList) {
        Map<Class, Drop> intangibleDrops = new HashMap<>();
        List<Drop> itemDrops = new ArrayList<>();
        for (String type : dropList) {
            if (Argument.valueOf(type).matchesArgumentType(ItemTag.class)) {
                BukkitItemStack bit = (BukkitItemStack) BukkitAdapter.adapt(Argument.valueOf(type).asType(ItemTag.class).getItemStack());
                ItemDrop drop = new ItemDrop("MMDA", null, bit);
                drop.setAmount(bit.getAmount());
                itemDrops.add(drop);
            } else if (Argument.valueOf(type).matchesPrimitive(PrimitiveType.String)) {
                Drop drop = Drop.getDrop("mmda_drop", type);
                if (drop instanceof IMultiDrop) {
                    LootBag loot = ((IMultiDrop) drop).get(new DropMetadata(event.getMob(), BukkitAdapter.adapt(event.getKiller())));
                    for (Drop d1 : loot.getDrops()) {
                        if (d1 instanceof IItemDrop) {
                            itemDrops.add(d1);
                        } else {
                            intangibleDrops.merge(d1.getClass(), d1, (o, n) -> o.addAmount(n));
                        }
                    }
                } else {
                    String[] arr1 = type.split(" ");
                    drop.setAmount(arr1.length == 1 ? 1.0D : Double.parseDouble(arr1[1]));
                    intangibleDrops.merge(drop.getClass(), drop, (o, n) -> o.addAmount(n));
                }
            }
        }
        lootBag.setLootTable(itemDrops);
        lootBag.setLootTableIntangible(intangibleDrops);
    }

    private ListTag getDrops(LootBag lootBag) {
        ListTag dropList = new ListTag();
        for (Drop drop : lootBag.getDrops()) {
            if (drop instanceof IItemDrop) {
                ItemStack item = BukkitAdapter.adapt(((IItemDrop) drop).getDrop(new DropMetadata(event.getMob(), BukkitAdapter.adapt(event.getKiller()))));
                dropList.add(new ItemTag(item).identify());
            }
            else if (drop instanceof ILocationDrop || drop instanceof IIntangibleDrop || drop instanceof IMessagingDrop) {
                dropList.add(drop.getLine());
            }
        }
        return dropList;
    }

    @Override
    public String getName() {
        return "MythicMobLootDropEvent";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, MythicMobsAddon.denizenInstance);
    }

    @Override
    public void destroy() {
        MythicMobLootDropEvent.getHandlerList().unregister(this);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        EntityTag dropper = new EntityTag(event.getEntity());
        return new BukkitScriptEntryData(dropper.isPlayer() ? dropper.getDenizenPlayer() : null, dropper.isNPC() ? dropper.getDenizenNPC() : null);
    }

    @Override
    public boolean applyDetermination(ScriptPath container, ObjectTag tag) {
        if (tag instanceof ListTag) {
            setDrops(lootBag, (ListTag) tag);
            return true;
        }
        return super.applyDetermination(container, tag);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name.toLowerCase()) {
            case "drops":
                return getDrops(lootBag);
            case "money":
                return new ElementTag(event.getMoney());
            case "exp":
                return new ElementTag(event.getExp());
            case "activemob":
                return new dActiveMob(event.getMob());
            case "killer":
                return new EntityTag(event.getKiller());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onMythicMobLootDrop(MythicMobLootDropEvent event) {
        this.event = event;
        this.lootBag = event.getDrops();
        fire();
    }
}
