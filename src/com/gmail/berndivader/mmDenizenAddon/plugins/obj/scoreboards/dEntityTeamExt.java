package com.gmail.berndivader.mmDenizenAddon.plugins.obj.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;
import com.gmail.berndivader.mmDenizenAddon.plugins.obj.dObjectExtension;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;

public class dEntityTeamExt extends dObjectExtension {
	private dEntity entity;
	
	
    public dEntityTeamExt(dEntity e) {
    	this.entity = e;
	}

	public static boolean describes(dObject object) {
        return object instanceof dEntity;
    }
    
    public static dEntityTeamExt getFrom(dObject o) {
    	if (!describes(o)) return null;
    	return new dEntityTeamExt((dEntity)o);
    }
    
	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
		if (m.matches("jointeam")) {
			ScoreBoardsAddon.EntityJoinTeam(this.entity, val.asString());
		} else if (m.matches("leaveteam")) {
			ScoreBoardsAddon.EntityLeaveTeam(this.entity);
		}
	}
    
    @Override
    public String getAttribute(Attribute a) {
    	Team team=null;
    	if (a.startsWith("hasteam")) {
    		team = (this.entity instanceof Player)?ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
    				:ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
    		return new Element(team!=null).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("team")) {
    		team = (this.entity instanceof Player)?ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
    				:ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
    		return new dTeam(team).getAttribute(a.fulfill(1));
    	}
        return new Element(this.entity.identify()).getAttribute(a);
    }
}
