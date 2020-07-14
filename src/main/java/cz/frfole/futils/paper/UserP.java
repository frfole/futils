package cz.frfole.futils.paper;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class UserP {
    private final CommandSender sender;

    public UserP(CommandSender sender) {
        this.sender = sender;
    }

    public Object getSender() {
        return sender;
    }

    public void sendMessage(String msg) {
        sender.sendMessage(msg);
    }

    public void sendMessage(List<String> msg) {
        sender.sendMessage(msg.toArray(new String[0]));
    }

    public void sendMessageRaw(String path) {
        sendMessage(MessageManagerP.get().gSL(path));
    }

    public void sendMsg(List<String> out) {
        if (MessageManagerP.get().getEnhance()) {
            for (String s : out) {
                try {
                    sender.sendMessage(ComponentSerializer.parse(s));
                } catch (Exception ex) {
                    sendMessage(s);
                }
            }
        } else {
            sendMessage(out);
        }
    }

    public void sendMsg(String path, UnaryOperator<String> uo) {
        List<String> out = MessageManagerP.Parser.parse(path, this);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
                out = PlaceholderAPI.setPlaceholders(player, out);
        }
        out.replaceAll(uo);
        sendMsg(out);
    }

    public void sendHelp(String path, int page, String cmd) {
        List<String> out = new ArrayList<>();
        List<String> header = MessageManagerP.Parser.parse(path + ".header", this);
        List<String> content = MessageManagerP.Parser.parse(path + ".content", this);
        List<String> footer = MessageManagerP.Parser.parse(path + ".footer", this);
        int lineLimit = MessageManagerP.get().getLineLimit();
        int pageMax = (content.size() - content.size() % lineLimit) / lineLimit + 1;
        page = page <= 0 ? 1 : page;
        page = (page - 1) * lineLimit > content.size() ? pageMax : page;
        content = content.subList(lineLimit * (page - 1), Math.min(lineLimit * page, content.size()));
        header = header.get(0).startsWith("PATH:") ? MessageManagerP.Parser.parse(header.get(0).substring(5).split(" ")[0], this) : header;
        footer = footer.get(0).startsWith("PATH:") ? MessageManagerP.Parser.parse(footer.get(0).substring(5).split(" ")[0], this) : footer;
        out.addAll(header);
        out.addAll(content);
        out.addAll(footer);
        int finalPage = page;
        out.replaceAll(s -> s.replaceAll("%page%", String.valueOf(finalPage))
                .replaceAll("%pageP%", String.valueOf(finalPage - 1))
                .replaceAll("%pageN%", String.valueOf(finalPage + 1))
                .replaceAll("%pageM%", String.valueOf(pageMax))
                .replaceAll("%cmd%", cmd));
        sendMsg(out);
    }

    public boolean hasPermission(String node) {
        return sender.hasPermission(node);
    }

    public boolean checkSendPerms(String node, String path) {
        if (!hasPermission(node))
            sendMsg(path, s -> s.replaceAll("%node%", node));
        return hasPermission(node);
    }

    public boolean checkSendPerms(String node) {
        return checkSendPerms(node, "error.noperms");
    }
}
