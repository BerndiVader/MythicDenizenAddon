package com.gmail.berndivader.mmDenizenAddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.berndivader.mmDenizenAddon.plugins.MythicMobsAddon;

public class MythicDenizenPlugin extends JavaPlugin {
	private static MythicDenizenPlugin plugin;
    private static SupportManager supportManager;
    public enum Dependings {
    	MythicMobs,
    	Denizen
    }
    
	public static MythicDenizenPlugin inst() {
		return plugin;
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		PluginManager pm = getServer().getPluginManager();
		if (pm.getPlugin(Dependings.Denizen.toString()) == null) {
			initFail("Denizen not found!");
			return;
		} else if (pm.getPlugin(Dependings.MythicMobs.toString()) == null) {
			initFail("MythicMobs not found!");
			return;
		}
    	String strMMVer = Bukkit.getServer().getPluginManager().getPlugin(Dependings.MythicMobs.toString()).getDescription().getVersion();
		int mmVer = Integer.valueOf(strMMVer.replaceAll("[\\D]", ""));
		if (mmVer < 400) {
			initFail("Only for MythicMobs 4 or higher!");
			return;
		}
		new RegisterEvents();
        supportManager = new SupportManager(plugin);
        try {
			supportManager.register(Support.setPlugin(MythicMobsAddon.class, pm.getPlugin(Dependings.MythicMobs.toString())));
		} catch (Exception e) {
			initError(e);
			return;
		}
        supportManager.registerNewObjects();
	}
	
	private void initFail(String reason) {
		getLogger().warning(reason);
		getPluginLoader().disablePlugin(this);
	}

	private void initError(Exception e) {
		getLogger().warning("There was a problem registering MythicMobs for Denizen!");
		e.printStackTrace();
		getPluginLoader().disablePlugin(this);
	}

	@Override
	public void onDisable() {
		plugin = null;
	}
}
