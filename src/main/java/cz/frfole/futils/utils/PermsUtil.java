package cz.frfole.futils.utils;

import cz.frfole.futils.CUser;

public class PermsUtil {

    /**
     * Check if user has permission and send him message if false
     *
     * @param user the user
     * @param node the permission node
     * @param path the path to the message
     * @return true if user has permission
     */
    public static boolean checkSendPerms(CUser user, String node, String path) {
        boolean out = user.getUser().hasPermission(node);
        if (!out)
            user.sendMsg(path, s -> s.replaceAll("%node%", node));
        return out;
    }

    /**
     * Check if user has permission and send him message at "error.noperms" if false
     *
     * @param user the user
     * @param node the permission node
     * @return true if user has permission
     */
    public static boolean checkSendPerms(CUser user, String node) {
        return checkSendPerms(user, node, "error.noperms");
    }
}
