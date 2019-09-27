package com.gmail.berndivader.mythicdenizenaddon.events;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class DConditionEvent extends SkillCondition implements IEntityCondition, ILocationCondition {

    private String conditionName;
    private String args;
    private boolean bool;

    public DConditionEvent(String line, MythicLineConfig mlc) {
        super(line);
        ACTION = ConditionAction.REQUIRED;
        conditionName = mlc.getString(new String[]{"condition", "c"}, "");
        args = mlc.getString(new String[]{"args", "a"}, "");
        bool = true;
    }

    @Override
    public boolean check(AbstractLocation loc) {
        Location l = BukkitAdapter.adapt(loc);
        MMDenizenConditionEvent e = new MMDenizenConditionEvent(null, l, this.conditionName, this.args, this.bool);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.getBool();
    }

    @Override
    public boolean check(AbstractEntity entity) {
        Entity ent = BukkitAdapter.adapt(entity);
        MMDenizenConditionEvent e = new MMDenizenConditionEvent(ent, null, this.conditionName, this.args, this.bool);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.getBool();
    }
}
