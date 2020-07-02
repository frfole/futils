package cz.frfole.futils;

import cz.frfole.futils.messages.Parser;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CUser {
    private final CommandSender user;
    private final Futils futils;

    public CUser(CommandSender user, Futils futils) {
        this.user = user;
        this.futils = futils;
    }

    /**
     * Sends {@code String} message to user
     *
     * @param msg the message
     */
    public void sendMessage(String msg) {
        this.user.sendMessage(msg);
    }

    /**
     * Sends {@link List<String>} to user
     *
     * @param strings the messages
     */
    public void sendMessage(List<String> strings) {
        this.user.sendMessage(strings.toArray(new String[0]));
    }

    /**
     * Sends raw message(S) from messages.yml to user
     *
     * @param path the path to message(s)
     */
    public void sendMsgR(String path) {
        this.sendMessage(this.futils.mM.gSL(path));
    }

    /**
     * Sends parsed message(s) from messages.yml to user
     *
     * @param path the path to message(s)
     */
    public void sendMsg(String path) {
        List<String> out = Parser.parse(path, this.futils, this.user);
        if (this.user instanceof Player) {
            Player player = (Player) this.user;
            if (this.futils.getCM().enablePAPI)
                out = PlaceholderAPI.setPlaceholders(player, out);
            if (this.futils.mM.isEnableEnhance()) {
                for (String s : out) {
                    try {
                        player.sendMessage(ComponentSerializer.parse(s));
                    } catch (Exception e) {
                        player.sendMessage(s);
                    }
                }
            } else {
                this.sendMessage(out);
            }
        } else {
            this.sendMessage(out);
        }
    }

    /**
     * Sends parsed help from messages.yml to user
     *
     * @param path  the path to help
     * @param index the page
     * @param cmd   the command
     */
    public void sendHelp(String path, int index, String cmd) {
        List<String> header = Parser.parse(path + ".header", this.futils, this.user);
        List<String> content = Parser.parse(path + ".content", this.futils, this.user);
        List<String> footer = Parser.parse(path + ".footer", this.futils, this.user);
        header = header.get(0).startsWith("PATH:") ? Parser.parse(header.get(0).substring(5).split(" ")[0], this.futils, this.user) : header;
        footer = footer.get(0).startsWith("PATH:") ? Parser.parse(footer.get(0).substring(5).split(" ")[0], this.futils, this.user) : footer;
        int pageMax = (content.size() - (content.size() % this.futils.mM.getLineLimit())) / this.futils.mM.getLineLimit() + 1;
        index = index <= 0 ? 1 : index;
        index = (index - 1) * this.futils.mM.getLineLimit() > content.size() ? pageMax : index;
        content = content.subList(this.futils.mM.getLineLimit() * (index - 1), Math.min(this.futils.mM.getLineLimit() * index, content.size()));
        List<String> out = new ArrayList<>();
        out.addAll(header);
        out.addAll(content);
        out.addAll(footer);
        int finalIndex = index;
        out.replaceAll(s -> s.replaceAll("%page%", String.valueOf(finalIndex))
                .replaceAll("%pageP%", String.valueOf(finalIndex - 1))
                .replaceAll("%pageN%", String.valueOf(finalIndex + 1))
                .replaceAll("%pageM%", String.valueOf(pageMax))
                .replaceAll("%cmd%", cmd));
        if (this.user instanceof Player) {
            Player player = (Player) this.user;
            if (this.futils.getCM().enablePAPI)
                out = PlaceholderAPI.setPlaceholders(player, out);
            if (this.futils.mM.isEnableEnhance()) {
                for (String s : out)
                    try {
                        player.sendMessage(ComponentSerializer.parse(s));
                    } catch (Exception e) {
                        player.sendMessage(s);
                    }
            } else {
                this.sendMessage(out);
            }
        } else {
            this.sendMessage(out);
        }
    }
}
