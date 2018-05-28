package com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dTeam implements dObject, Adjustable {

	private static String id="team@";
	private String prefix=id;
	private Team team;
	
	public dTeam(Team scoreboardTeam) {

		if (scoreboardTeam==null) return;
		this.team = scoreboardTeam;
	}
	
    public static boolean matches(String string) {
        return valueOf(string) != null;
    }
    
	public static dTeam valueOf(String name) {
		return valueOf(name, null);
	}
	
	@Override
	public void adjust(Mechanism m) {
		Element val = m.getValue();
		switch(m.getName().toLowerCase()) {
		case "addmember":
			if (m.requireObject(dEntity.class)) {
				Entity entity = val.asType(dEntity.class).getBukkitEntity();
				team.addEntry((entity instanceof Player)?entity.getName():entity.getUniqueId().toString());
			}
			break;
		case "delmember":
			String s1 = val.asString();
			if (this.team.hasEntry(s1)) this.team.removeEntry(s1);
			break;
		case "displayname":
			this.team.setDisplayName(val.asString());
			break;
		case "collision":
			try {
				this.team.setOption(Option.COLLISION_RULE, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
			break;
		case "deathmessage":
			try {
				this.team.setOption(Option.DEATH_MESSAGE_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
			break;
		case "nametag":
			try {
				this.team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
			break;
		case "friendlyfire":
			this.team.setCanSeeFriendlyInvisibles(val.asBoolean());
			break;
		case "prefix":
			this.team.setPrefix(val.asString());
			break;
		case "suffix":
			this.team.setSuffix(val.asString());
			break;
		}
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		if (a.startsWith("name")) {
			return new Element(this.team.getName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("displayname")) {
			return new Element(this.team.getDisplayName()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("members")) {
			return ScoreBoardsAddon.getAllMembersOfTeam(this.team).getAttribute(a.fulfill(1)); 
		} else if (a.startsWith("collision")) {
			OptionStatus oStat = this.team.getOption(Option.COLLISION_RULE);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("deathmessage")) {
			OptionStatus oStat = this.team.getOption(Option.DEATH_MESSAGE_VISIBILITY);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("nametag")) {
			OptionStatus oStat = this.team.getOption(Option.NAME_TAG_VISIBILITY);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("friendlyfire")) {
			return new Element(this.team.allowFriendlyFire()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("friendlyinvisibles")) {
			return new Element(this.team.canSeeFriendlyInvisibles()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("prefix")) {
			return new Element(this.team.getPrefix()).getAttribute(a.fulfill(1));
		} else if (a.startsWith("suffix")) {
			return new Element(this.team.getSuffix()).getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a.fulfill(0));
	}

	@Override
	public void applyProperty(Mechanism mech) {
	}

	@Override
	public String debug() {
		return prefix+"='<A>"+identify()+"<G>'";
	}

	@Override
	public String getObjectType() {
		return "ScoreboardTeam";
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String identify() {
		return this.team!=null?this.team.getName():null;
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

    @Fetchable("team")
    public static dTeam valueOf(String string, TagContext context) {
        if (string!=null) {
            return new dTeam(ScoreBoardsAddon.scoreboard.getTeam(string.replace(id,"")));
        }
        return null;
    }
}
