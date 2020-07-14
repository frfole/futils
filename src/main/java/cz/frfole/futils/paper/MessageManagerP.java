package cz.frfole.futils.paper;

import cz.frfole.futils.universal.YamlConf;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageManagerP {
    private static MessageManagerP instance;
    private final YamlConf conf = new YamlConf();
    private boolean enhance = true;
    private int lineLimit = 10;
    private File confFile;

    public static MessageManagerP get() {
        return instance == null ? new MessageManagerP() : instance;
    }

    public boolean getEnhance() {
        return enhance;
    }

    public int getLineLimit() {
        return lineLimit;
    }

    public YamlConf getConf() {
        return conf;
    }

    public File getConfFile() {
        return confFile;
    }

    public boolean setup(JavaPlugin plugin) {
        instance = this;
        try {
            confFile = new File(plugin.getDataFolder(), "messages.yml");
            if (!confFile.getParentFile().exists())
                confFile.getParentFile().mkdirs();
            if (!confFile.exists())
                plugin.saveResource("messages.yml", true);
            conf.load(confFile);
            enhance = conf.getBoolean("enhance", true);
            lineLimit = conf.getInt("lineLimit", 10);
            return true;
        } catch (FileNotFoundException e) {
            plugin.getSLF4JLogger().error("Failed to find messages.yml!");
            return false;
        }
    }

    public List<String> gSL(String path, List<String> def) {
        if (!conf.isList(path))
            return conf.isString(path) ? Collections.singletonList(conf.getString(path)) : def;
        return conf.getStringList(path);
    }

    public List<String> gSL(String path) {
        return gSL(path, Collections.singletonList(""));
    }

    public String gS(String path, String def) {
        return conf.getString(path, def);
    }

    public String gS(String path) {
        return conf.getString(path, "");
    }

    public static class Parser {
        public static List<String> parse(String path, UserP user) {
            List<String> out0 = new ArrayList<>();
            for (String s : MessageManagerP.get().gSL(path)) {
                if (!needPerms(s, user)) {
                    s = removePerms(s);
                    s = parseMsg(s);
                    out0.add(s);
                }
            }
            return out0;
        }

        private static String parseMsg(String in) {
            while (in.contains("{START|msg:") && in.contains(":msg|END}"))
                in = in.replace("{START|" + getParse(in)[0] + "|END}", MessageManagerP.get().gS(getParse(in, "msg")[0].replaceAll("msg:", "").replaceAll(":msg", "")));
            return in;
        }

        private static boolean needPerms(String in, UserP user) {
            while (true) {
                if (in.contains("{START|perms:") && in.contains(":perms|END}")) {
                    String perm = getParse(in, "parms")[0].replaceAll("perms:", "").replaceAll(":perms", "");
                    in = in.replace("{START|perms:" + perm + ":perms|END}", "");
                    if (((Permissible) user.getSender()).hasPermission(perm))
                        continue;
                    return true;
                } else {
                    return false;
                }
            }
        }

        public static String removePerms(String in) {
            while (in.contains("{START|perms") && in.contains("perms|END}"))
                in = getParse(in, "perms")[1];
            return in;
        }

        private static String[] getParse(String in, String type) {
            if (in.contains("{START|" + type) && in.contains(type + "|END}")) {
                String out0 = in.substring(in.indexOf("{START|" + type), in.indexOf(type + "|END}") + 5 + type.length());
                return new String[]{out0.substring(7, out0.length() - 5), in.replace(out0, "")};
            } else {
                return new String[]{"", ""};
            }
        }

        private static String[] getParse(String in) {
            if (in.contains("{START|") && in.contains("|END}")) {
                String out0 = in.substring(in.indexOf("{START|"), in.indexOf("|END}") + 5);
                return new String[]{out0.substring(7, out0.length() - 5), in.replace(out0, "")};
            } else {
                return new String[]{"", ""};
            }
        }
    }
}
