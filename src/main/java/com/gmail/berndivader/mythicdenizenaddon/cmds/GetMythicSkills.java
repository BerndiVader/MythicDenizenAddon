package com.gmail.berndivader.mythicdenizenaddon.cmds;

import com.denizenscript.denizencore.exceptions.CommandExecutionException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.StaticStrings;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicSkill;
import io.lumine.xikage.mythicmobs.skills.Skill;

import java.util.Iterator;
import java.util.regex.Pattern;

public class GetMythicSkills extends AbstractCommand {

    @Override
    public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
        for (Argument arg : entry.getProcessedArgs()) {
            if (!entry.hasObject(StaticStrings.FILTER) && arg.matchesPrefix(StaticStrings.FILTER)) {
                entry.addObject(StaticStrings.FILTER, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.STRICT) && arg.matchesPrefix(StaticStrings.STRICT)) {
                entry.addObject(StaticStrings.STRICT, arg.asElement());
            } else if (!entry.hasObject(StaticStrings.DATA) && arg.matchesPrefix(StaticStrings.DATA)) {
                entry.addObject(StaticStrings.DATA, arg.asType(dMythicMeta.class));
            }
        }

        entry.defaultObject(StaticStrings.FILTER, new ElementTag(""));
        entry.defaultObject(StaticStrings.STRICT, new ElementTag(false));
    }

    @Override
    public void execute(ScriptEntry entry) throws CommandExecutionException {
        Pattern p = Pattern.compile(entry.getElement(StaticStrings.FILTER).asString());
        String metaId = entry.hasObject(StaticStrings.DATA) ? entry.getObjectTag(StaticStrings.DATA).identify() : null;

        if (!entry.getElement(StaticStrings.STRICT).asBoolean()) {
            Iterator<Skill> it = MythicMobsAddon.mythicSkillManager.getSkills().iterator();
            ListTag list = new ListTag();
            while (it.hasNext()) {
                String s1 = it.next().getInternalName();
                if (p.matcher(s1).find()) {
                    dMythicSkill skill = new dMythicSkill(s1, metaId);
                    if (skill.isPresent()) {
                        list.add(skill.identify());
                    }
                }
            }
            entry.addObject("skills", list);
        } else {
            dMythicSkill skill = new dMythicSkill(p.pattern(), metaId);
            if (skill.isPresent()) {
                entry.addObject("skill", skill);
            } else {
                throw new CommandExecutionException("Failed to create " + dMythicSkill.class.getSimpleName());
            }
        }
    }
}
