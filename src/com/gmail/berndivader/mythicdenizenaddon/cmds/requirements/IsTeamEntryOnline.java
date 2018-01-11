package com.gmail.berndivader.mythicdenizenaddon.cmds.requirements;

import java.util.List;

import com.gmail.berndivader.mythicdenizenaddon.ScoreBoardsAddon;

import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;
import net.aufdemrand.denizen.scripts.requirements.RequirementsContext;

public class IsTeamEntryOnline extends AbstractRequirement {

	@Override
	public boolean check(RequirementsContext context, List<String> args) throws RequirementCheckException {
		if (args.size() < 1) return false;
		String entry = args.get(0);
		if (ScoreBoardsAddon.getEntityByEntry(entry)!=null) return true;
		return false;
	}
}
