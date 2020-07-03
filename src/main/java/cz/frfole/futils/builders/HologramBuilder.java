package cz.frfole.futils.builders;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HologramBuilder {
    private final Hologram hd;

    public HologramBuilder(JavaPlugin plugin, Location loc) {
        hd = HologramsAPI.createHologram(plugin, loc);
    }

    public Hologram build() {
        return this.hd;
    }

    public HologramBuilder addLine(ItemStack item) {
        this.hd.appendItemLine(item);
        return this;
    }

    public HologramBuilder addLine(String line) {
        this.hd.appendTextLine(line);
        return this;
    }
}
