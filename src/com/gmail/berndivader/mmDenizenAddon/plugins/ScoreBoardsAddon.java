package com.gmail.berndivader.mmDenizenAddon.plugins;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.gmail.berndivader.mmDenizenAddon.Support;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.requirements.isTeamEntryOnline;
import com.gmail.berndivader.mmDenizenAddon.plugins.cmds.scoreboards.*;
import com.gmail.berndivader.mmDenizenAddon.plugins.obj.scoreboards.*;

import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;

public class ScoreBoardsAddon extends Support {
	
	public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	
	@SuppressWarnings("unchecked")
	public ScoreBoardsAddon() {

		registerObjects(dTeam.class);
		registerProperty(dEntityTeamExt.class, dEntity.class);
		
		new isTeamEntryOnline().activate().as("isentryonline").withOptions("- isentryonline [entry:string]", 1);

		new createTeam().activate().as("createteam").withOptions("- createteam [name:string]", 1);
		new removeTeam().activate().as("removeteam").withOptions("- removeteam [name:string]", 1);
		new getAllTeams().activate().as("getallteams").withOptions("- getallteams", 0);
		new getTeam().activate().as("getteam").withOptions("- getteam [name:string]", 1);
		new getEntityByEntry().as("getentitybyentry").withOptions("- getentitybyentry [entry:string]", 1);
	}

	public static dList getAllMembersOfTeam(Team team) {
		dList list = new dList();
		for (String s : team.getEntries()) {
			try {
				UUID uuid = UUID.fromString(s);
				dEntity.getEntityForID(uuid);
			} catch (Exception ex) {
				
			}
			list.add(new Element(s).identify());
		}
		return list;
	}

	public static dList getAllTeamsOf(Scoreboard sb) {
		dList list = new dList();
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
			entity = dEntity.getEntityForID(uuid);
		} catch (Exception ex) {
			entity = (Entity)Bukkit.getPlayer(entry);
		}
		return entity;
	}

	public static void EntityJoinTeam(dEntity de, String tname) {
		Entity entity = de.getBukkitEntity();
		Team team = scoreboard.getTeam(tname);
		if (team==null) return;
		team.addEntry((entity instanceof Player)?entity.getName():entity.getUniqueId().toString());
	}

	public static void EntityLeaveTeam(dEntity de) {
		Entity entity = de.getBukkitEntity();
		String entry = (entity instanceof Player)?entity.getName():entity.getUniqueId().toString();
		Team team = scoreboard.getEntryTeam(entry);
		if (team!=null) team.removeEntry(entry);
	}
}
