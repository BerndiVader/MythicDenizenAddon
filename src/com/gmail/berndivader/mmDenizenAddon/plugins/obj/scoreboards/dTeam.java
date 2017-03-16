package com.gmail.berndivader.mmDenizenAddon.plugins.obj.scoreboards;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Adjustable;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.Fetchable;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;

public class dTeam implements dObject, Adjustable {

	private String prefix;
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
	    // @adjust <dTeam> addmember:<dEntity>
		if (m.matches("addmember") && m.requireObject(dEntity.class)) {
			Entity entity = val.asType(dEntity.class).getBukkitEntity();
			team.addEntry((entity instanceof Player)?entity.getName():entity.getUniqueId().toString());
	    // @adjust <dTeam> delmember:<entry>
		} else if (m.matches("delmember")) {
			String ss = val.asString();
			if (this.team.hasEntry(ss)) this.team.removeEntry(ss);
	    // @adjust <dTeam> displayname:<String>
		} else if (m.matches("displayname")) {
			this.team.setDisplayName(val.asString());
	    // @adjust <dTeam> collision:<NEVER||ALWAYS||FOR_OWN_TEAM||FOR_OTHER_TEAMS>
		} else if (m.matches("collision")) {
			try {
				this.team.setOption(Option.COLLISION_RULE, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
	    // @adjust <dTeam> deathmessage:<NEVER||ALWAYS||FOR_OWN_TEAM||FOR_OTHER_TEAMS>
		} else if (m.matches("deathmessage")) {
			try {
				this.team.setOption(Option.DEATH_MESSAGE_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
	    // @adjust <dTeam> nametag:<NEVER||ALWAYS||FOR_OWN_TEAM||FOR_OTHER_TEAMS>
		} else if (m.matches("nametag")) {
			try {
				this.team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
			} catch (IllegalArgumentException ex) {/** empty */}
	    // @adjust <dTeam> friendlyinvisibles:<boolean>
		} else if (m.matches("friendlyinvisibles") && val.isBoolean()) {
			this.team.setCanSeeFriendlyInvisibles(val.asBoolean());
	    // @adjust <dTeam> friendlyfire:<boolean>
		} else if (m.matches("friendlyfire") && val.isBoolean()) {
			this.team.setAllowFriendlyFire(val.asBoolean());
	    // @adjust <dTeam> prefix:<String>
		} else if (m.matches("prefix")) {
			this.team.setPrefix(val.asString());
	    // @adjust <dTeam> suffix:<String>
		} else if (m.matches("suffix")) {
			this.team.setSuffix(val.asString());
		}
	}

	@Override
	public String getAttribute(Attribute a) {
		if (a==null) return null;
		// @attribute <team.name>
		// @returns Element<String>
		if (a.startsWith("name")) {
			return new Element(this.team.getName()).getAttribute(a.fulfill(1));
		// @attribute <team.displayname>
		// @returns Element<String>
		} else if (a.startsWith("displayname")) {
			return new Element(this.team.getDisplayName()).getAttribute(a.fulfill(1));
		// @attribute <team.members>
		// @returns dList<String>
		} else if (a.startsWith("members")) {
			return ScoreBoardsAddon.getAllMembersOfTeam(this.team).getAttribute(a.fulfill(1)); 
		// @attribute <team.collision>
		// @returns Element<String>
		// possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
		} else if (a.startsWith("collision")) {
			OptionStatus oStat = this.team.getOption(Option.COLLISION_RULE);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		// @attribute <team.deathmessage>
		// @returns Element<String>
		// possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
		} else if (a.startsWith("deathmessage")) {
			OptionStatus oStat = this.team.getOption(Option.DEATH_MESSAGE_VISIBILITY);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		// @attribute <team.nametag>
		// @returns Element<String>
		// possible values: ALWAYS || NEVER || FOR_OTHER_TEAMS || FOR_OWN_TEAM
		} else if (a.startsWith("nametag")) {
			OptionStatus oStat = this.team.getOption(Option.NAME_TAG_VISIBILITY);
			return new Element(oStat.toString()).getAttribute(a.fulfill(1));
		// @attribute <team.friendlyfire>
		// @returns Element<Boolean>
		} else if (a.startsWith("friendlyfire")) {
			return new Element(this.team.allowFriendlyFire()).getAttribute(a.fulfill(1));
		// @attribute <team.friendlyinvisibles>
		// @returns Element<Boolean>
		} else if (a.startsWith("friendlyinvisibles")) {
			return new Element(this.team.canSeeFriendlyInvisibles()).getAttribute(a.fulfill(1));
		// @attribute <team.prefix>
		// @returns Element<String>
		} else if (a.startsWith("prefix")) {
			return new Element(this.team.getPrefix()).getAttribute(a.fulfill(1));
		// @attribute <team.suffix>
		// @returns Element<String>
		} else if (a.startsWith("suffix")) {
			return new Element(this.team.getSuffix()).getAttribute(a.fulfill(1));
		}
		return new Element(identify()).getAttribute(a);
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
		return "team@" + this.team.getName();
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
        if (string == null) return null;
        return new dTeam(ScoreBoardsAddon.scoreboard.getTeam(string.replace("team@", "")));
    }
}
