package com.gmail.berndivader.mythicdenizenaddon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public
class 
MythicDenizenPlugin
extends
JavaPlugin 
{
	static MythicDenizenPlugin instance;
	MythicMobsAddon addon;
	
    enum Dependings {
    	MythicMobs,
    	Denizen,
    	Quests
    }
    
	public static MythicDenizenPlugin inst() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance=this;
		PluginManager pm = getServer().getPluginManager();
		if (pm.getPlugin(Dependings.Denizen.toString()) == null) {
			initFail("Denizen not found!");
			return;
		} else if (pm.getPlugin(Dependings.MythicMobs.toString()) == null) {
			initFail("MythicMobs not found!");
			return;
		} else if(pm.getPlugin(Dependings.Quests.name())!=null) {
			writeDenizenCustomObjective();
		}
    	String strMMVer = Bukkit.getServer().getPluginManager().getPlugin(Dependings.MythicMobs.toString()).getDescription().getVersion();
		int mmVer = Integer.valueOf(strMMVer.replaceAll("[\\D]", ""));
		if (mmVer < 400) {
			initFail("Only for MythicMobs 4 or higher!");
			return;
		}
        try {
    		addon=new MythicMobsAddon();
    		new RegisterEvents();
		} catch (Exception e) {
			initError(e);
			return;
		}
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
	
	static void writeDenizenCustomObjective() {
		File target=new File(instance.getDataFolder().toString().replace("\\"+instance.getName(),"")+"/Quests/modules/DenizenCustomQuestsObjective.jar");
		if(!target.exists()) {
			URL url=instance.getClassLoader().getResource("DenizenCustomQuestsObjective.jar");
			try {
				InputStream in_stream=url.openStream();
				try (FileOutputStream out_stream=new FileOutputStream(target)){
					byte[]buffer=new byte[1024];
					int i1;
					while((i1=in_stream.read(buffer))!=-1){
						out_stream.write(buffer,0,i1);
					}
				};
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisable() {
		instance=null;
	}
}
