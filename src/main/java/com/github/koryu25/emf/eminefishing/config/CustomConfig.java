package com.github.koryu25.emf.eminefishing.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class CustomConfig {

    //InstanceField
    private FileConfiguration config = null;
    private final File configFile;
    private final String file;
    private final Plugin plugin;

    //Constructor
    public CustomConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = fileName;
        this.configFile = new File(plugin.getDataFolder(), this.file);
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            plugin.saveResource(this.file, false);
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) return;
        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig() {
        if (this.config == null) reloadConfig();
        return config;
    }

    public void saveConfig() {
        if (config == null) return;
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
}
