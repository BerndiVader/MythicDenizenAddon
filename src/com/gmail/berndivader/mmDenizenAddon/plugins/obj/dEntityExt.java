package com.gmail.berndivader.mmDenizenAddon.plugins.obj;


import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmDenizenAddon.plugins.MythicMobsAddon;

import io.lumine.xikage.mythicmobs.MythicMobs;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dEntityExt extends dObjectExtension {
	private dEntity entity;
	
	
    public dEntityExt(dEntity e) {
    	this.entity = e;
	}

	public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }
    
    public static dEntityExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dEntityExt((dEntity)o);
    }
    
	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
	    // @adjust <dEntity> followrange <double>
		if (m.matches("followrange") && m.requireDouble()) {
    		if (this.entity.isLivingEntity()) {
				LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
       				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE)!=null) {
       					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
       					ai.setBaseValue(val.asDouble());
       				};
    		    } else {
    		    	MythicMobs.inst().getVolatileCodeHandler().setFollowRange(le, val.asDouble());
    		    }
    		}
   	    // @adjust <dEntity> damage <double>
		} else if (m.matches("damage") && m.requireDouble()) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
    					ai.setBaseValue(val.asDouble());
    				}
    			} else {
    				MythicMobs.inst().getVolatileCodeHandler().setAttackDamage(le, val.asDouble());
    			}
    		}
   	    // @adjust <dEntity> armor <double>
    	// only for > 1.9 
		} else if (m.matches("armor") && m.requireDouble()) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
    					ai.setBaseValue(val.asDouble());
    				}
    			}
    		}
   	    // @adjust <dEntity> attackspeed <double>
    	// only for > 1.9
		} else if (m.matches("attackspeed") && m.requireDouble()) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
    					ai.setBaseValue(val.asDouble());
    				}
    			}
    		}
   	    // @adjust <dEntity> knockbackresist <double>
		} else if (m.matches("knockbackresist") && m.requireDouble()) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
    					ai.setBaseValue(val.asDouble());
    				}
    			} else {
    				MythicMobs.inst().getVolatileCodeHandler().setKnockBackResistance(le, val.asDouble());
    			}
    		}
		}
	}
	
    @Override
    public String getAttribute(Attribute a) {

    	if (a.startsWith("isactivemob")) {
    		return new Element(MythicMobsAddon.isActiveMob(entity.getUUID())).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("activemob") && MythicMobsAddon.isActiveMob(entity.getUUID())) {
    		return new dActiveMob(MythicMobsAddon.getActiveMob(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
  	    // @attribute <entity.damage>
        // @returns Element(double)
    	} else if (a.startsWith("damage")) {
    		if (this.entity.isLivingEntity()) {
        		LivingEntity le = this.entity.getLivingEntity();
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
               		if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE)!=null) {
               			AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
               		};
    			}
    		}
  	    // @attribute <entity.followrange>
        // @returns Element(double)
    	} else if (a.startsWith("followrange")) {
    		if (this.entity.isLivingEntity() && MythicMobs.inst().getMinecraftVersion()>=9) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE)!=null) {
    				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE);
           			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
    			}
    		}
   	    // @attribute <entity.armor>
        // @returns Element(double)
    	} else if (a.startsWith("armor")) {
    		if (this.entity.isLivingEntity() && MythicMobs.inst().getMinecraftVersion()>=9) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR)!=null) {
    				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
           			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
    			}
    		}
   	    // @attribute <entity.attackspeed>
        // @returns Element(double)
    	} else if (a.startsWith("attackspeed")) {
    		if (this.entity.isLivingEntity() && MythicMobs.inst().getMinecraftVersion()>=9) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED)!=null) {
    				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
           			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
    			}
    		}
   	    // @attribute <entity.knockbackresist>
        // @returns Element(double)
    	} else if (a.startsWith("knockbackresist")) {
    		if (this.entity.isLivingEntity() && MythicMobs.inst().getMinecraftVersion()>=9) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE)!=null) {
    				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
           			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
    			}
    		}
  	    // @attribute <entity.jumpstrength>
        // @returns Element(double)
        //  returns jump strength if entity is a horse type
    	} else if (a.startsWith("jumpstrength")) {
    		if (this.entity.isLivingEntity() && MythicMobs.inst().getMinecraftVersion()>=9) {
    			LivingEntity le = this.entity.getLivingEntity();
    			if (le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH)!=null) {
    				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH);
           			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
    			}
    		}
  	    // @attribute <entity.maxnodamageticks>
        // @returns Element(integer)
    	} else if (a.startsWith("maxnodamageticks")) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
       			return new Element(le.getMaximumNoDamageTicks()).getAttribute(a.fulfill(1));
    		}
  	    // @attribute <entity.nodamageticks>
        // @returns Element(integer)
    	} else if (a.startsWith("nodamageticks")) {
    		if (this.entity.isLivingEntity()) {
    			LivingEntity le = this.entity.getLivingEntity();
       			return new Element(le.getNoDamageTicks()).getAttribute(a.fulfill(1));
    		}
    	}
        return new Element(this.entity.identify()).getAttribute(a);
    }
}
