package cz.frfole.futils.files;

import cz.frfole.futils.Futils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final Futils futils;
    private final LinkedHashMap<String, Configuration> configs = new LinkedHashMap<>();
    public boolean enablePAPI = false;

    /**
     * Construct ConfigManager
     *
     * @param plugin the source plugin
     * @param futils the {@link Futils} instance
     */
    public ConfigManager(JavaPlugin plugin, Futils futils) {
        this.plugin = plugin;
        this.futils = futils;
    }

    /**
     * Construct ConfigManager with defined files
     *
     * @param files  defined files
     * @param plugin the source plugin
     * @param futils the {@link Futils} instance
     */
    public ConfigManager(Map<String, File> files, JavaPlugin plugin, Futils futils) {
        this.plugin = plugin;
        this.futils = futils;
        for (Map.Entry<String, File> entry : files.entrySet())
            newFile(entry.getValue(), entry.getKey());
    }

    /**
     * Returns {@link Configuration}
     *
     * @param name access name
     * @return {@link Configuration}
     */
    public Configuration getConfig(String name) {
        return this.configs.get(name);
    }

    /**
     * Creates and adds <b>Configuration</b> to <b>ConfigManager</b>
     *
     * @param file file
     * @param name access name
     */
    public void newFile(File file, String name) {
        configs.put(name, new Configuration(file, this.plugin));
    }

    /**
     * Invokes {@link Configuration#load()} for {@link #configs}.get(name).
     *
     * @param name access name
     */
    public void loadFile(String name) {
        configs.get(name).load();
    }

    /**
     * Invokes {@link Configuration#load()} for all values in {@link #configs}
     */
    public void loadAll() {
        for (Configuration config : this.configs.values())
            config.load();
    }

    /**
     * returns the value of path in name
     *
     * @param name the access name
     * @param path the path
     * @return the value of path in name
     */
    public String getString(String name, String path) {
        return configs.get(name).getConfig().getString(path, "");
    }

    /**
     * returns the value or def of path in name
     *
     * @param name the access name
     * @param path the path
     * @param def  the default value
     * @return the value or def of path in name
     */
    public String getString(String name, String path, String def) {
        return configs.get(name).getConfig().getString(path, def);
    }

    /**
     * returns list of strings of path in name
     *
     * @param name the access name
     * @param path the path
     * @return list of strings of path in name
     */
    public List<String> getStringList(String name, String path) {
        return configs.get(name).getConfig().getStringList(path);
    }
}
