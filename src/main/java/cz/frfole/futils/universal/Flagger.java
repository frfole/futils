package cz.frfole.futils.universal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Flagger {
    private static final Pattern quotesPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    public static Map<String, Object> getFlags(Map<String, String> flagsType, String in) {
        Map<String, Object> out = new HashMap<>();
        String[] args = getArgs(in);
        for (int i = 0; i < args.length - 1; i++) {
            for (Map.Entry<String, String> entry : flagsType.entrySet()) {
                if (args[i].equals(entry.getKey())) {
                    switch (entry.getValue().toLowerCase()) {
                        default:
                            continue;
                        case "b":
                        case "bool":
                        case "boolean":
                            out.put(entry.getKey(), getBool(args[i + 1], true));
                            break;
                        case "i":
                        case "int":
                        case "integer":
                            try {
                                out.put(entry.getKey(), Integer.valueOf(args[i + 1]));
                            } catch (Exception ignored) {
                            }
                            break;
                        case "l":
                        case "long":
                            try {
                                out.put(entry.getKey(), Long.valueOf(args[i + 1]));
                            } catch (Exception ignored) {
                            }
                            break;
                        case "s":
                        case "string":
                            out.put(entry.getKey(), args[i + 1]);
                            break;
                    }
                }
            }
        }
        return out;
    }

    private static String[] getArgs(String s) {
        Matcher m = quotesPattern.matcher(s);
        List<String> argsList = new ArrayList<>();
        while (m.find())
            argsList.add(m.group(1));
        return argsList.toArray(new String[0]);
    }

    private static boolean getBool(String s, boolean def) {
        switch (s.toLowerCase()) {
            case "1":
            case "t":
            case "yes":
            case "true":
                return true;
            case "0":
            case "f":
            case "no":
            case "false":
                return false;
            default:
                return def;
        }
    }

    private static boolean getBool(String s) {
        return getBool(s, false);
    }
}
