package com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards;

import com.denizenscript.denizen.utilities.ScoreboardHelper;
import com.denizenscript.denizencore.objects.Adjustable;
import com.denizenscript.denizencore.objects.Fetchable;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.tags.TagContext;
import org.bukkit.scoreboard.Scoreboard;

public class dScoreboard implements ObjectTag, Adjustable {

    public static boolean matches(String string) {
        return valueOf(string) != null;
    }

    public static dScoreboard valueOf(String name) {
        return valueOf(name, null);
    }

    @Fetchable("scoreboard")
    public static dScoreboard valueOf(String string, TagContext context) {
        if (string == null) {
            return null;
        }
        String id = string.replace("scoreboard@", "");
        return new dScoreboard(ScoreboardHelper.getScoreboard(id), id);
    }

    private String prefix;
    private String id;
    private Scoreboard scoreboard;

    public dScoreboard(Scoreboard board, String id) {
        if (board == null) {
            return;
        }
        this.id = id;
        this.scoreboard = board;
    }

    @Override
    public String getObjectType() {
        return "Scoreboard";
    }

    @Override
    public String debug() {
        return prefix + "='<A>" + identify() + "<G>'";
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
        return "scoreboard@" + this.id;
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

        if (attribute.matches("id")) {
            return new ElementTag(this.id).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void applyProperty(Mechanism mechanism) {
        adjust(mechanism);
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // This does nothing?
        // ElementTag val = m.getValue();
    }
}
