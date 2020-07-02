package cz.frfole.futils.messages;

import cz.frfole.futils.Futils;
import cz.frfole.futils.files.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageManager {
    private final Futils futils;
    private Configuration msgConf;
    private boolean enableEnhance = false;
    private int lineLimit = 10;

    /**
     * Constructs the {@link MessageManager}
     *
     * @param futils the futils
     */
    public MessageManager(Futils futils) {
        this.futils = futils;
        this.load();
    }

    /**
     * loads messages.yml file
     */
    public void load() {
        this.msgConf = this.futils.getCM().getConfig("messages");
        this.msgConf.load();
        this.enableEnhance = this.msgConf.getConfig().getBoolean("enableEnhance", false);
        this.lineLimit = this.msgConf.getConfig().getInt("helpLineLimit", 10);
    }

    /**
     * Returns true if is enhanced messages enabled
     *
     * @return true if is enhanced messages enabled
     */
    public boolean isEnableEnhance() {
        return enableEnhance;
    }

    /**
     * Returns line limit for help
     *
     * @return line limit for help
     */
    public int getLineLimit() {
        return this.lineLimit;
    }

    /**
     * Returns string list in messages.yml
     *
     * @param path the path
     * @param def0 the default string list
     * @return string list in messages.yml
     */
    public List<String> gSL(String path, String def0) {
        if (!this.msgConf.getConfig().isList(path)) {

            return this.msgConf.getConfig().isString(path)
                    ? new ArrayList<>(Collections.singletonList(this.msgConf.getConfig().getString(path)))
                    : new ArrayList<>(Collections.singleton(def0));
        }
        return this.msgConf.getConfig().getStringList(path);
    }

    /**
     * Returns string list in messages.yml
     *
     * @param path the path
     * @return string list in messages.yml
     */
    public List<String> gSL(String path) {
        return this.gSL(path, "");
    }

    /**
     * Returns string in messages.yml
     *
     * @param path the path
     * @param def0 the default string
     * @return string in messages.yml
     */
    public String gS(String path, String def0) {
        return this.msgConf.getConfig().getString(path, def0);
    }

    /**
     * Returns string in messages.yml
     *
     * @param path the path
     * @return string in messages.yml
     */
    public String gS(String path) {
        return this.gS(path, "");
    }
}
