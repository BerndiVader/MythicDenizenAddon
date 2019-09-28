package com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.dObjectExtension;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class dEntityTeamExt extends dObjectExtension {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag;
    }

    public static dEntityTeamExt getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        return new dEntityTeamExt((EntityTag) entity);
    }

    private EntityTag entity;

    public dEntityTeamExt(EntityTag entity) {
        this.entity = entity;
    }

    @Override
    public String getAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        Team team;
        if (attribute.startsWith("hasteam")) {
            team = (this.entity instanceof Player) ? ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
                    : ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
            return new ElementTag(team != null).getAttribute(attribute.fulfill(1));
        } else if (attribute.startsWith("team")) {
            team = (this.entity instanceof Player) ? ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getName())
                    : ScoreBoardsAddon.scoreboard.getEntryTeam(this.entity.getUUID().toString());
            return new dTeam(team).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {
        ElementTag val = mechanism.getValue();
        switch (mechanism.getName().toLowerCase()) {
            case "jointeam":
                ScoreBoardsAddon.EntityJoinTeam(this.entity, val.asString());
            case "leaveteam":
                ScoreBoardsAddon.EntityLeaveTeam(this.entity);
        }
    }
}
