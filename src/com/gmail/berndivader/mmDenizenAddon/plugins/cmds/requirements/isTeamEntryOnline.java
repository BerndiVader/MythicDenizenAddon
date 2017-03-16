package com.gmail.berndivader.mmDenizenAddon.plugins.cmds.requirements;

import java.util.List;

import com.gmail.berndivader.mmDenizenAddon.plugins.ScoreBoardsAddon;

import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;
import net.aufdemrand.denizen.scripts.requirements.RequirementsContext;

public class isTeamEntryOnline extends AbstractRequirement {

	@Override
	public boolean check(RequirementsContext context, List<String> args) throws RequirementCheckException {
		if (args.size() < 1) return false;
		String entry = args.get(0);
		if (ScoreBoardsAddon.getEntityByEntry(entry)!=null) return true;
		return false;
	}
}
