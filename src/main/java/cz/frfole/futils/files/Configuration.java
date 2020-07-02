package cz.frfole.futils.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Configuration {
    private final File file;
    private final JavaPlugin plugin;
    private final YamlConfiguration config = new YamlConfiguration();

    // constructor
    public Configuration(File file, JavaPlugin plugin) {
        this.file = file;
        this.plugin = plugin;
    }

    // getters and setters
    public YamlConfiguration getConfig() {
        return this.config;
    }

    // methods
    public boolean load(boolean replace) {
        if (!this.file.getParentFile().exists())
            if (!this.file.getParentFile().mkdirs())
                return false;
        if (!this.file.exists())
            this.plugin.saveResource(file.getName(), replace);
        try {
            this.config.load(this.file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean load() {
        return load(false);
    }
}
