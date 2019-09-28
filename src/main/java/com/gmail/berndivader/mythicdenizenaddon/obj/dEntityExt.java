package com.gmail.berndivader.mythicdenizenaddon.obj;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public class dEntityExt extends dObjectExtension {

    private EntityTag entity;

    public dEntityExt(EntityTag e) {
        this.entity = e;
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }

    public static dEntityExt getFrom(ObjectTag o) {
        if (!describes(o)) {
            return null;
        }
        return new dEntityExt((EntityTag) o);
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("is_activemob") || attribute.startsWith("isactivemob")) {
            return new ElementTag(MythicMobsAddon.isActiveMob(entity.getUUID())).getAttribute(attribute.fulfill(1));
        } else if ((attribute.startsWith("activemob") || attribute.startsWith("get_activemob")) && MythicMobsAddon.isActiveMob(entity.getUUID())) {
            return new dActiveMob(MythicMobsAddon.getActiveMob(entity.getBukkitEntity())).getAttribute(attribute.fulfill(1));
        }

        if (this.entity.isLivingEntity()) {
            LivingEntity le = this.entity.getLivingEntity();
            if (attribute.startsWith("damage")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("followrange")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("armor")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("attackspeed")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("knockbackresist")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("jumpstrength")) {
                if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                    if (le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH) != null) {
                        AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH);
                        return new ElementTag(ai.getBaseValue()).getAttribute(attribute.fulfill(1));
                    }
                }
            } else if (attribute.startsWith("maxnodamageticks")) {
                return new ElementTag(le.getMaximumNoDamageTicks()).getAttribute(attribute.fulfill(1));
            } else if (attribute.startsWith("nodamageticks")) {
                return new ElementTag(le.getNoDamageTicks()).getAttribute(attribute.fulfill(1));
            }
        }

        if (attribute.startsWith("mmtargets") || attribute.startsWith("mm_targeter") && attribute.hasContext(1)) {
            return MythicMobsAddon.getTargetsFor(this.entity.getBukkitEntity(), attribute.getContext(1)).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        if (mechanism.requireDouble() && this.entity.isLivingEntity()) {
            ElementTag val = mechanism.getValue();
            LivingEntity le = this.entity.getLivingEntity();
            switch (mechanism.getName().toLowerCase()) {
                case "followrange":
                    if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                        if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE) != null) {
                            AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
                            ai.setBaseValue(val.asDouble());
                        }
                        ;
                    } else {
                        MythicMobs.inst().getVolatileCodeHandler().setFollowRange(le, val.asDouble());
                    }
                    break;
                case "damage":
                    if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                        if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                            AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
                            ai.setBaseValue(val.asDouble());
                        }
                    } else {
                        MythicMobs.inst().getVolatileCodeHandler().setAttackDamage(le, val.asDouble());
                    }
                    break;
                case "armor":
                    if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                        if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR) != null) {
                            AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
                            ai.setBaseValue(val.asDouble());
                        }
                    }
                    break;
                case "attackspeed":
                    if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                        if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED) != null) {
                            AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
                            ai.setBaseValue(val.asDouble());
                        }
                    }
                    break;
                case "knockbackresist":
                    if (MythicMobs.inst().getMinecraftVersion() >= 9) {
                        if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
                            AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                            ai.setBaseValue(val.asDouble());
                        }
                    } else {
                        MythicMobs.inst().getVolatileCodeHandler().setKnockBackResistance(le, val.asDouble());
                    }
                    break;
            }
        }
    }
}
