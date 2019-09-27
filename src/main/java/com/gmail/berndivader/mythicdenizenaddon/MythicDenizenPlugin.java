package com.gmail.berndivader.mythicdenizenaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MythicDenizenPlugin extends JavaPlugin {

    public static MythicDenizenPlugin instance;
    public MythicMobsAddon addon;

    static class Dependencies {
        static String MYTHIC_MOBS = "MythicMobs";
        static String DENIZEN = "Denizen";
        static String QUESTS = "Quests";
    }

    public static MythicDenizenPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();

        if (pm.getPlugin(Dependencies.DENIZEN) == null) {
            initFail("Denizen not found!");
            return;
        }
        if (pm.getPlugin(Dependencies.MYTHIC_MOBS) == null) {
            initFail("MythicMobs not found!");
            return;
        }
        if (pm.getPlugin(Dependencies.QUESTS) != null) {
            writeDenizenCustomObjective();
        }

        String strMMVer = Bukkit.getServer().getPluginManager().getPlugin(Dependencies.DENIZEN).getDescription().getVersion();
        int mmVer = Integer.parseInt(strMMVer.replaceAll("[\\D]", ""));
        if (mmVer < 400) {
            initFail("Only for MythicMobs 4 or higher!");
            return;
        }

        try {
            addon = new MythicMobsAddon();
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
        File target = new File(instance.getDataFolder().toString().replace("\\" + instance.getName(), "")
                + "/Quests/modules/DenizenCustomQuestsObjective.jar");

        if (!target.exists()) {
            URL url = instance.getClassLoader().getResource("DenizenCustomQuestsObjective.jar");
            try {
                InputStream in_stream = url.openStream();
                try (FileOutputStream out_stream = new FileOutputStream(target)) {
                    byte[] buffer = new byte[1024];
                    int i1 = in_stream.read(buffer);
                    while (i1 != -1) {
                        out_stream.write(buffer, 0, i1);
                        i1 = in_stream.read(buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
