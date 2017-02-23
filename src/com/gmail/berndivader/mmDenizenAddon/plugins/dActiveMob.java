package com.gmail.berndivader.mmDenizenAddon.plugins;

import java.util.UUID;

import org.bukkit.Bukkit;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dActiveMob implements dObject, Adjustable {
	
	private String prefix;
	ActiveMob am;

	public dActiveMob(ActiveMob activeMob) {
		if (activeMob==null) return;
		this.am = activeMob;
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dActiveMob valueOf(String uuid) {
		return valueOf(uuid, null);
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
		if (m.matches("global_cooldown") && m.requireInteger()) {
			this.am.setGlobalCooldown(val.asInt());
		} else if (m.matches("remove_self")) {
			MythicMobsAddon.removeSelf(this.am);
		} 
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		
		if (a.startsWith("internal_name")) 
			return new Element(am.getType().getInternalName()).getAttribute(a.fulfill(1));
		else if (a.startsWith("display_name")) 
			return new Element(am.getType().getDisplayName()).getAttribute(a.fulfill(1));
		else if (a.startsWith("type"))
			return new Element("ActiveMob").getAttribute(a.fulfill(1));
		
		return new Element(identify()).getAttribute(a);
	}

	@Override
	public void applyProperty(Mechanism mech) {
		Bukkit.getLogger().warning("Cannot apply properties to a ActiveMob.");
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "ActiveMob";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return "activemob@" + this.am.getUniqueId();
	}

	@Override
	public String identifySimple() {
		return identify();
	}

	@Override
	public boolean isUnique() {
		return true;
	}

	@Override
	public dObject setPrefix(String string) {
		this.prefix = string;
		return this;
	}

    @Fetchable("activemob")
    public static dActiveMob valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        try {
            string = string.replace("activemob@", "");
            UUID uuid = UUID.fromString(string);
            if (!MythicMobsAddon.isActiveMob(uuid)) {
                return null;
            }
            return new dActiveMob(MythicMobsAddon.getActiveMob(dEntity.getEntityForID(uuid)));
        }
        catch (Exception e) {
            return null;
        }
    }
}
