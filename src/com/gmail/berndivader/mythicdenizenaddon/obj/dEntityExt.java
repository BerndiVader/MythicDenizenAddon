package com.gmail.berndivader.mythicdenizenaddon.obj;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;

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
		if (m.requireDouble()&&this.entity.isLivingEntity()) {
			LivingEntity le=this.entity.getLivingEntity();
			switch(m.getName().toLowerCase()) {
			case "followrange":
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
       				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE)!=null) {
       					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
       					ai.setBaseValue(val.asDouble());
       				};
    		    } else {
    		    	MythicMobs.inst().getVolatileCodeHandler().setFollowRange(le, val.asDouble());
    		    }
				break;
			case "damage":
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
    					ai.setBaseValue(val.asDouble());
    				}
    			} else {
    				MythicMobs.inst().getVolatileCodeHandler().setAttackDamage(le, val.asDouble());
    			}
				break;
			case "armor":
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
    					ai.setBaseValue(val.asDouble());
    				}
    			}
				break;
			case "attackspeed":
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED)!=null) {
    					AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
    					ai.setBaseValue(val.asDouble());
    				}
    			}
				break;
			case "knockbackresist":
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
    				if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE)!=null) {
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
	
    @Override
    public String getAttribute(Attribute a) {
    	if (a.startsWith("is_activemob")||a.startsWith("isactivemob")) {
    		return new Element(MythicMobsAddon.isActiveMob(entity.getUUID())).getAttribute(a.fulfill(1));
    	} else if ((a.startsWith("activemob")||a.startsWith("get_activemob"))&&MythicMobsAddon.isActiveMob(entity.getUUID())) {
    		return new dActiveMob(MythicMobsAddon.getActiveMob(entity.getBukkitEntity())).getAttribute(a.fulfill(1));
    	}
    	if (this.entity.isLivingEntity()) {
    		LivingEntity le=this.entity.getLivingEntity();
        	if (a.startsWith("damage")) {
    			if (MythicMobs.inst().getMinecraftVersion()>=9) {
               		if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE)!=null) {
               			AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
               		};
    			}
        	} else if (a.startsWith("followrange")) {
        		if (MythicMobs.inst().getMinecraftVersion()>=9) {
        			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE)!=null) {
        				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_FOLLOW_RANGE);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
        			}
        		}
        	} else if (a.startsWith("armor")) {
        		if (MythicMobs.inst().getMinecraftVersion()>=9) {
        			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR)!=null) {
        				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ARMOR);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
        			}
        		}
        	} else if (a.startsWith("attackspeed")) {
        		if (MythicMobs.inst().getMinecraftVersion()>=9) {
        			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED)!=null) {
        				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_SPEED);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
        			}
        		}
        	} else if (a.startsWith("knockbackresist")) {
        		if (MythicMobs.inst().getMinecraftVersion()>=9) {
        			if (le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE)!=null) {
        				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.GENERIC_KNOCKBACK_RESISTANCE);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
        			}
        		}
        	} else if (a.startsWith("jumpstrength")) {
        		if (MythicMobs.inst().getMinecraftVersion()>=9) {
        			if (le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH)!=null) {
        				AttributeInstance ai = le.getAttribute(org.bukkit.attribute.Attribute.HORSE_JUMP_STRENGTH);
               			return new Element(ai.getBaseValue()).getAttribute(a.fulfill(1));
        			}
        		}
        	} else if (a.startsWith("maxnodamageticks")) {
       			return new Element(le.getMaximumNoDamageTicks()).getAttribute(a.fulfill(1));
        	} else if (a.startsWith("nodamageticks")) {
       			return new Element(le.getNoDamageTicks()).getAttribute(a.fulfill(1));
        	}
    	}
    	if (a.startsWith("mmtargets")||a.startsWith("mm_targeter") && a.hasContext(1)) {
			return MythicMobsAddon.getTargetsFor(this.entity.getBukkitEntity(), a.getContext(1)).getAttribute(a.fulfill(1));
    	}
		return new Element(this.entity!=null?this.entity.identify():null).getAttribute(a.fulfill(0));
    }
}
