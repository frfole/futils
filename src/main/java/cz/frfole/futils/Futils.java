package cz.frfole.futils;

import cz.frfole.futils.files.ConfigManager;
import cz.frfole.futils.messages.MessageManager;
import cz.frfole.futils.utils.HDUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Futils {
    public final MessageManager mM;
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    public Futils(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin, this);
        aLF("messages");
        aLF("config");
        this.getCM().enablePAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        this.mM = new MessageManager(this);
        HDUtil.setStatus(plugin.getServer().getPluginManager().getPlugin("HolographicDisplays") != null);
    }

    /**
     * Returns {@link ConfigManager}
     *
     * @return {@link ConfigManager}
     */
    public ConfigManager getCM() {
        return this.configManager;
    }

    /**
     * Adds .yml file to {@link #configManager} and load it
     *
     * @param name the name of file without <b>.yml</b>
     */
    public void aLF(String name) {
        File file = new File(this.plugin.getDataFolder(), name + ".yml");
        this.configManager.newFile(file, name);
        this.configManager.getConfig(name).load();
    }

    /**
     * Reloads Futils
     */
    public void reload() {
        this.getCM().loadAll();
        this.getCM().enablePAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}
