package com.gmail.berndivader.mythicdenizenaddon.events;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DSpawnConditionEvent extends SkillCondition implements ILocationCondition {

    private String ConditionName;
    private String ConditionArgs;
    private boolean bool;

    public DSpawnConditionEvent(String line, MythicLineConfig mlc) {
        super(line);
        this.ACTION = ConditionAction.REQUIRED;
        this.ConditionName = mlc.getString(new String[]{"condition", "c"}, "");
        this.ConditionArgs = mlc.getString(new String[]{"args", "a"}, "");
        this.bool = true;
    }

    @Override
    public boolean check(AbstractLocation loc) {
        Location l = BukkitAdapter.adapt(loc);
        MMDenizenConditionEvent e = new MMDenizenConditionEvent(null, l, this.ConditionName, this.ConditionArgs, this.bool);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.getBool();
    }
}
