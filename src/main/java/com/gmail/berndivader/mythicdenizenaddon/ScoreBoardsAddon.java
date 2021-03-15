package com.gmail.berndivader.mythicdenizenaddon;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.ObjectFetcher;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.scripts.commands.CommandRegistry;
import com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards.CreateTeam;
import com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards.GetAllTeams;
import com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards.GetEntityByEntry;
import com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards.GetTeam;
import com.gmail.berndivader.mythicdenizenaddon.cmds.scoreboards.RemoveTeam;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dEntityTeamExt;
import com.gmail.berndivader.mythicdenizenaddon.obj.scoreboards.dTeam;

public
class
ScoreBoardsAddon
{
	public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	public static Denizen denizen=Denizen.getInstance();
	static CommandRegistry commandregistry=denizen.getCommandRegistry();
	
	public ScoreBoardsAddon() {
		
		ObjectFetcher.registerWithObjectFetcher(dTeam.class);
		
		PropertyParser.registerProperty(dEntityTeamExt.class,EntityTag.class);
		
		commandregistry.registerCoreMember(CreateTeam.class,"createteam","createteam [name:string]",1);
		commandregistry.registerCoreMember(GetAllTeams.class,"getallteams","getallteams",0);
		commandregistry.registerCoreMember(RemoveTeam.class,"removeteam","removeteam [name:string]",1);
		commandregistry.registerCoreMember(GetTeam.class,"getteam","getteam [name:string]",1);
		commandregistry.registerCoreMember(GetEntityByEntry.class,"getentitybyentry","getentitybyentry [entry:string]",1);
	}

	public static ListTag getAllMembersOfTeam(Team team) {
		ListTag list = new ListTag();
		for (String s : team.getEntries()) {
			try {
				UUID uuid = UUID.fromString(s);
				EntityTag.getEntityForID(uuid);
			} catch (Exception ex) {
				
			}
			list.add(new ElementTag(s).identify());
		}
		return list;
	}

	public static ListTag getAllTeamsOf(Scoreboard sb) {
		ListTag list = new ListTag();
		for (Team t : sb.getTeams()) {
			list.add(new dTeam(t).identify());
		}
		return list;
	}

	public static boolean scoreBoardHasTeam(String teamName) {
		return scoreboard.getTeam(trimmedTeamName(teamName))!=null;
	}

	public static dTeam getTeam(String teamName) {
		return new dTeam(scoreboard.getTeam(trimmedTeamName(teamName)));
	}
	
	public static String trimmedTeamName(String name) {
		if (name.length()>16) name=name.substring(0, 15);
		return name;
	}
	
	public static Entity getEntityByEntry(String entry) {
		Entity entity = null;
		try {
			UUID uuid = UUID.fromString(entry);
			entity = EntityTag.getEntityForID(uuid);
		} catch (Exception ex) {
			entity = (Entity)Bukkit.getPlayer(entry);
		}
		return entity;
	}

	public static void EntityJoinTeam(EntityTag de, String tname) {
		Entity entity = de.getBukkitEntity();
		Team team = scoreboard.getTeam(tname);
		if (team==null) return;
		team.addEntry((entity instanceof Player)?entity.getName():entity.getUniqueId().toString());
	}

	public static void EntityLeaveTeam(EntityTag de) {
		Entity entity = de.getBukkitEntity();
		String entry = (entity instanceof Player)?entity.getName():entity.getUniqueId().toString();
		Team team = scoreboard.getEntryTeam(entry);
		if (team!=null) team.removeEntry(entry);
	}
}
