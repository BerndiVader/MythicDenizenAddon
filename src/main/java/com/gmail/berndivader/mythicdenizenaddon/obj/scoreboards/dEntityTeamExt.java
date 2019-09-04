package com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.dObjectExtension;

public
class 
dEntityTeamExt 
extends 
dObjectExtension
{
	private EntityTag entity;
	
    public dEntityTeamExt(EntityTag e) {
    	this.entity = e;
	}

	public static boolean describes(ObjectTag object) {
        return object instanceof EntityTag;
    }
    
    public static dEntityTeamExt getFrom(ObjectTag o) {
    	if (!describes(o)) return null;
    	return new dEntityTeamExt((EntityTag)o);
    }
    
	@Override
	public void adjust(Mechanism m) {
		ElementTag val = m.getValue();
		switch(m.getName().toLowerCase()) {
		case "jointeam":
			ScoreBoardsAddon.EntityJoinTeam(this.entity, val.asString());
		case "leaveteam":
			ScoreBoardsAddon.EntityLeaveTeam(this.entity);
		}
	}
    
    @Override
    public String getAttribute(Attribute a) {
    	if(a==null) return null;
    	Team team=null;
    	if (a.startsWith("hasteam")) {
    		team=(this.entity instanceof Player)?ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
    				:ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
    		return new ElementTag(team!=null).getAttribute(a.fulfill(1));
    	} else if (a.startsWith("team")) {
    		team = (this.entity instanceof Player)?ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
    				:ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
    		return new dTeam(team).getAttribute(a.fulfill(1));
    	}
    	return null;
    }
}
