package com.gmail.berndivader.mythicdenizenaddon.obj;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import org.bukkit.entity.Entity;

public class ActivePlayer implements SkillCaster {

    private AbstractEntity entity;
    private boolean useDamageSkill;
    private int level;

    public ActivePlayer(Entity entity) {
        this.entity = BukkitAdapter.adapt(entity);
        this.useDamageSkill = false;
        this.level = 0;
    }

    @Override
    public AbstractEntity getEntity() {
        return this.entity;
    }

    @Override
    public AbstractLocation getLocation() {
        return this.entity.getLocation();
    }

    @Override
    public void setUsingDamageSkill(boolean isUsingSkill) {
        this.useDamageSkill = isUsingSkill;
    }

    @Override
    public boolean isUsingDamageSkill() {
        return this.useDamageSkill;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public float getPower() {
        // TODO: Auto-generated method stub
        return 0;
    }
}
