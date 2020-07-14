package cz.frfole.futils.paper.utils;

import cz.frfole.futils.paper.UserP;

public class Help {
    public static boolean help(UserP user, String path, String[] args, String cmd, int index) {
        if (args.length > index) {
            try {
                user.sendHelp(path, Integer.parseInt(args[index]), cmd);
                return true;
            } catch (Exception ex0) {
                if (args[index].equals("help") || args[index].equals("h")) {
                    try {
                        user.sendHelp(path, Integer.parseInt(args[index + 1]), cmd);
                        return true;
                    } catch (Exception ex1) {
                        user.sendHelp(path, 1, cmd);
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            user.sendHelp(path, 1, cmd);
            return true;
        }
    }
}
