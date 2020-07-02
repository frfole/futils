package cz.frfole.futils.messages;

import cz.frfole.futils.Futils;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    /**
     * Returns parsed string list from messages.yml
     *
     * @param path   the path
     * @param futils the {@link Futils} instance
     * @param user   the user
     * @return parsed string list from messages.yml
     */
    public static List<String> parse(String path, Futils futils, Permissible user) {
        List<String> out0 = new ArrayList<>();
        for (String s : futils.mM.gSL(path)) {
            if (needPerms(s, user))
                continue;
            s = removePerms(s);
            s = parseMsg(s, futils.mM);

            out0.add(s);
        }
        return out0;
    }

    /**
     * Returns replaced {@code {START|msg:PATH:msg|END}} with message at PATH
     *
     * @param in the input string
     * @param mM the {@link MessageManager} instance
     * @return replaced {@code {START|msg:PATH:msg|END}} with message at PATH
     */
    private static String parseMsg(String in, MessageManager mM) {
        while (in.contains("{START|msg:") && in.contains(":msg|END}")) {
            in = in.replace("{START|" + getParse(in)[0] + "|END}"
                    , mM.gS(getParse(in, "msg")[0].replaceAll("msg:", "").replaceAll(":msg", "")));
        }
        return in;
    }

    /**
     * Check if user needs permission to show string
     *
     * @param in   the input string
     * @param user the user
     * @return true if user requires permission
     */
    public static boolean needPerms(String in, Permissible user) {
        while (in.contains("{START|perms:") && in.contains(":perms|END}")) {
            String perm = getParse(in, "perms")[0].replaceAll("perms:", "").replaceAll(":perms", "");
            in = in.replace("{START|perms:" + perm + ":perms|END}", "");
            if (!user.hasPermission(perm))
                return true;
        }
        return false;
    }

    /**
     * Returns replaced {@code {START|perms:NODE:perms|END}} with ""
     *
     * @param in the input
     * @return replaced {@code {START|perms:NODE:perms|END}} with ""
     */
    public static String removePerms(String in) {
        while (in.contains("{START|perms") && in.contains("perms|END}")) {
            in = getParse(in, "perms")[1];
        }
        return in;
    }

    // I do not know, what this does
    private static String[] getParse(String in) {
        if (in.contains("{START|") && in.contains("|END}")) {
            String out0 = in.substring(in.indexOf("{START|"), in.indexOf("|END}") + 5);
            return new String[]{out0.substring(7, out0.length() - 5), in.replace(out0, "")};
        }
        return new String[]{"", ""};
    }

    // I do not know, what this does
    private static String[] getParse(String in, String type) {
        if (in.contains("{START|" + type) && in.contains(type + "|END}")) {
            String out0 = in.substring(in.indexOf("{START|" + type), in.indexOf(type + "|END}") + 5 + type.length());
            return new String[]{out0.substring(7, out0.length() - 5), in.replace(out0, "")};
        }
        return new String[]{"", ""};
    }
}
