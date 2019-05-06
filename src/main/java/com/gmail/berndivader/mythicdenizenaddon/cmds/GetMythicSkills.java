package com.gmail.berndivader.mythicdenizenaddon.cmds;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.gmail.berndivader.mythicdenizenaddon.MythicMobsAddon;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicMeta;
import com.gmail.berndivader.mythicdenizenaddon.obj.dMythicSkill;

import io.lumine.xikage.mythicmobs.skills.Skill;
import net.aufdemrand.denizencore.exceptions.CommandExecutionException;
import net.aufdemrand.denizencore.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.scripts.ScriptEntry;
import net.aufdemrand.denizencore.scripts.commands.AbstractCommand;

public class GetMythicSkills
extends
AbstractCommand {
	
	@Override
	public void parseArgs(ScriptEntry entry) throws InvalidArgumentsException {
		for (aH.Argument arg:aH.interpret(entry.getArguments())) {
			if(!entry.hasObject("filter")&&arg.matchesPrefix("filter")) {
				entry.addObject("filter",arg.asElement());
			} else if(!entry.hasObject("strict")&&arg.matchesPrefix("strict")) {
				entry.addObject("strict",arg.asElement());
			}
			if(!entry.hasObject("data")&&arg.matchesPrefix("data")) {
				entry.addObject("data",arg.asType(dMythicMeta.class));
			}
		}
		if (!entry.hasObject("filter")) entry.addObject("filter",new Element(new String("")));
		if (!entry.hasObject("strict")) entry.addObject("strict",new Element(false));
	}
	
	@Override
	public void execute(ScriptEntry entry) throws CommandExecutionException {
		Pattern p=Pattern.compile(entry.getElement("filter").asString());
		String metaId=entry.hasObject("data")?entry.getdObject("data").identify():null;
		if (!entry.getElement("strict").asBoolean()) {
			Iterator<Skill>it=MythicMobsAddon.mythicmobs.getSkillManager().getSkills().iterator();
			dList list=new dList();
			while(it.hasNext()) {
				String s1=it.next().getInternalName();
				if (p.matcher(s1).find()) {
					dMythicSkill skill=new dMythicSkill(s1,metaId);
					if (skill.isPresent()) list.add(skill.identify());
				}
			}
			entry.addObject("skills",list);
		} else {
			dMythicSkill skill=new dMythicSkill(p.pattern(),metaId);
			if(skill.isPresent()) {
				entry.addObject("skill",skill);
			} else {
				throw new CommandExecutionException("Failed to create "+dMythicSkill.class.getSimpleName());
			}
		}
	}
}
