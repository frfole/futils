package cz.frfole.futils.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import java.util.ArrayList;
import java.util.List;

public class HDUtil {
    public static List<Hologram> holograms = new ArrayList<>();
    private static boolean enable;

    public static void setStatus(boolean b) {
        enable = b;
    }

    public static void removeHDs() {
        if (enable)
            holograms.forEach(Hologram::delete);
    }

    public static void createHD(Hologram hd) {
        if (enable)
            holograms.add(hd);
    }
}
