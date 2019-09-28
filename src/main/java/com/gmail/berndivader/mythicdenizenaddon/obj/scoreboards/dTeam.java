package com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

public class dTeam implements ObjectTag, Adjustable {

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dTeam valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("team")
    public static dTeam valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        return new dTeam(ScoreBoardsAddon.scoreboard.getTeam(string.replace(id, "")));
    }

    private static String id = "team@";
    private String prefix = id;
    private Team team;

    public dTeam(Team scoreboardTeam) {
        if (scoreboardTeam == null) {
            return;
        }
        this.team = scoreboardTeam;
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>'";
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
    public ObjectTag setPrefix(String string) {
        this.prefix = string;
        return this;
    }

    @Override
    public String identify() {
        return this.team != null ? this.team.getName() : null;
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
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("name")) {
            return new ElementTag(this.team.getName()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("displayname")) {
            return new ElementTag(this.team.getDisplayName()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("members")) {
            return ScoreBoardsAddon.getAllMembersOfTeam(this.team).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("collision")) {
            OptionStatus oStat = this.team.getOption(Option.COLLISION_RULE);
            return new ElementTag(oStat.toString()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("deathmessage")) {
            OptionStatus oStat = this.team.getOption(Option.DEATH_MESSAGE_VISIBILITY);
            return new ElementTag(oStat.toString()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("nametag")) {
            OptionStatus oStat = this.team.getOption(Option.NAME_TAG_VISIBILITY);
            return new ElementTag(oStat.toString()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("friendlyfire")) {
            return new ElementTag(this.team.allowFriendlyFire()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("friendlyinvisibles")) {
            return new ElementTag(this.team.canSeeFriendlyInvisibles()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("prefix")) {
            return new ElementTag(this.team.getPrefix()).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("suffix")) {
            return new ElementTag(this.team.getSuffix()).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag val = mechanism.getValue();
        switch (mechanism.getName().toLowerCase()) {
            case "addmember":
                if (mechanism.requireObject(EntityTag.class)) {
                    Entity entity = val.asType(EntityTag.class).getBukkitEntity();
                    team.addEntry((entity instanceof Player) ? entity.getName() : entity.getUniqueId().toString());
                }
                break;
            case "delmember":
                String s1 = val.asString();
                if (this.team.hasEntry(s1)) {
                    this.team.removeEntry(s1);
                }
                break;
            case "displayname":
                this.team.setDisplayName(val.asString());
                break;
            case "collision":
                try {
                    this.team.setOption(Option.COLLISION_RULE, OptionStatus.valueOf(val.asString().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    // Do nothing
                }
                break;
            case "deathmessage":
                try {
                    this.team.setOption(Option.DEATH_MESSAGE_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    // Do nothing
                }
                break;
            case "nametag":
                try {
                    this.team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.valueOf(val.asString().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    // Do nothing
                }
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
}
