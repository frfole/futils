package cz.frfole.futils.universal;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class YamlConf {
    private final Yaml yaml;
    private Object objects;

    public YamlConf() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndent(2);
        dumperOptions.setPrettyFlow(true);
        yaml = new Yaml(dumperOptions);
    }

    public void load(File file) throws FileNotFoundException {
        objects = yaml.load(new FileInputStream(file));
    }

    public void save(File file) throws IOException {
        yaml.dump(objects, new FileWriter(file));
    }

    public Object get(String path, Object def) {
        if (path == null)
            return null;
        if (path.length() <= 0)
            return this;
        String[] keys = path.split("\\.");
        Object out;
        Object ob = objects;
        for (String key : keys) {
            try {
                ob = ((Map<String, Object>) ob).get(key);
            } catch (Exception ignored) {
            }
        }
        out = ob;
        if (ob == null)
            return def;
        return out;
    }

    public void set(String path, Object o) {
        if (path == null) return;
        if (path.length() <= 0) return;
        ArrayList<String> keys = new ArrayList<>(Arrays.asList(path.split("\\.")));
        String lastKey = keys.remove(keys.size() - 1);
        Object ob = objects;
        for (String key : keys) {
            try {
                if (((Map<String, Object>) ob).containsKey(key)) {
                    ob = ((Map<String, Object>) ob).get(key);
                } else {
                    ((Map<String, Object>) ob).put(key, new HashMap<String, Object>());
                    ob = ((Map<String, Object>) ob).get(key);
                }
            } catch (Exception ignored) {
            }
        }
        ((Map<String, Object>) ob).put(lastKey, o);
    }


    public String getString(String path, String def) {
        Object o = get(path, def);
        if (!(o instanceof String))
            return def;
        return (String) o;
    }

    public String getString(String path) {
        return getString(path, null);
    }

    public boolean isString(String path) {
        return get(path, null) instanceof String;
    }

    public boolean getBoolean(String path, boolean def) {
        Object o = get(path, def);
        if (!(o instanceof Boolean))
            return def;
        return (Boolean) o;
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public int getInt(String path, int def) {
        Object o = get(path, def);
        if (!(o instanceof Integer))
            return def;
        return (Integer) o;
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }


    public List<?> getList(String path, List<?> def) {
        Object o = get(path, def);
        if (!(o instanceof List))
            return null;
        return (List<?>) o;
    }

    public List<?> getList(String path) {
        return getList(path, new ArrayList<>());
    }

    public boolean isList(String path) {
        return get(path, null) instanceof List;
    }

    public List<String> getStringList(String path, List<String> def) {
        return (List<String>) getList(path, def);
    }

    public List<String> getStringList(String path) {
        return getStringList(path, new ArrayList<String>());
    }
}
