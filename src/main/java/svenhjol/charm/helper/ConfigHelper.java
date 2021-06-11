package svenhjol.charm.helper;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.moandjiezana.toml.Toml;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.module.CharmModule;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class ConfigHelper {

    @Nullable
    public static Toml getConfig(String mod) {
        String configPath = "./config/" + mod + ".toml";
        Path path = Paths.get(configPath);
        File file = path.toFile();

        if (!file.exists())
            return null;

        return new Toml().read(path.toFile());
    }

    public static boolean isModuleDisabled(Toml config, String moduleName) {
        String moduleEnabled = moduleName + " Enabled";
        String moduleEnabledQuoted = "\"" + moduleEnabled + "\"";
        return config.contains(moduleEnabledQuoted) && !config.getBoolean(moduleEnabledQuoted);
    }

    public static void createConfig(String mod, Map<String, CharmModule> modules) {
        String configPath = "./config/" + mod + ".toml";

        // this blank config is appended and then written out. LinkedHashMap supplier sorts the contents alphabetically
        CommentedConfig writeConfig = TomlFormat.newConfig(LinkedHashMap::new);
        Path path = Paths.get(configPath);
        File file = path.toFile();
        Toml readConfig = file.exists() ? new Toml().read(path.toFile()) : new Toml();

        List<String> moduleNames = new ArrayList<>(modules.keySet());
        Collections.sort(moduleNames);

        // parse config and apply values to modules
        for (String moduleName : moduleNames) {
            CharmModule module = modules.get(moduleName);

            // set module enabled/disabled
            String moduleEnabled = moduleName + " Enabled";
            String moduleEnabledQuoted = "\"" + moduleEnabled + "\"";
            module.enabled = readConfig.contains(moduleEnabledQuoted) ? readConfig.getBoolean(moduleEnabledQuoted) : module.enabledByDefault;

            if (!module.alwaysEnabled) {
                writeConfig.setComment(moduleEnabled, module.description);
                writeConfig.add(moduleEnabled, module.enabled);
            }

            // get and set module config options
            ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
            classFields.forEach(classField -> {
                try {
                    Config annotation = classField.getDeclaredAnnotation(Config.class);
                    if (annotation == null)
                        return;

                    // set the static property as writable so that the config can modify it
                    classField.setAccessible(true);
                    String name = annotation.name();
                    String description = annotation.description();

                    if (name.isEmpty())
                        name = classField.getName();

                    Object classValue = classField.get(null);
                    Object configValue = null;

                    if (readConfig.contains(moduleName)) {

                        // get the block of key/value pairs from the config
                        Toml moduleKeys = readConfig.getTable(moduleName);
                        Map<String, Object> mappedKeys = new HashMap<>();

                        // key names sometimes have quotes, map to remove them
                        moduleKeys.toMap().forEach((k, v) -> mappedKeys.put(k.replace("\"", ""), v));
                        configValue = mappedKeys.get(name);

                        if (configValue != null) {

                            // there's some weirdness with casting, deal with that here
                            if (classValue instanceof Integer && configValue instanceof Double)
                                configValue = (int)(double) configValue;

                            if (classValue instanceof Integer && configValue instanceof Long)
                                configValue = (int)(long) configValue;

                            classField.set(null, configValue);
                        }
                    }

                    if (configValue == null)
                        configValue = classValue;

                    // set the key/value pair. The "." specifies that it is nested
                    String moduleConfigName = moduleName + "." + name;
                    writeConfig.setComment(moduleConfigName, description);
                    writeConfig.add(moduleConfigName, configValue);

                } catch (Exception e) {
                    Charm.LOG.error("Failed to set config for " + moduleName + ": " + e.getMessage());
                }
            });
        }

        try {
            // write out and close the file
            TomlWriter tomlWriter = new TomlWriter();
            Writer buffer = Files.newBufferedWriter(path);
            tomlWriter.write(writeConfig, buffer);
            buffer.close();
            Charm.LOG.debug("Written config to disk");

        } catch (Exception e) {
            Charm.LOG.error("Failed to write config: " + e.getMessage());
        }
    }


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @see <a href="https://stackoverflow.com/a/520344">Stackoverflow inspiration</a>
     * @param packageName The base package
     * @return fully qualified class name strings
     */
    public static List<String> getClassesInPackage(String packageName) throws IOException, URISyntaxException {
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;

        ArrayList<String> classes = new ArrayList<>();

        URL url = new URL(classLoader.getResource(path).toString());

        if (url.toString().startsWith("jar:")) {
            try {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                File file = new File(connection.getJarFileURL().toURI());

                packageName = packageName.replaceAll("\\.", "/");
                try {
                    JarInputStream jarFile = new JarInputStream(new FileInputStream(file));
                    JarEntry jarEntry;

                    while (true) {
                        jarEntry = jarFile.getNextJarEntry();
                        if (jarEntry == null) {
                            break;
                        }
                        if (jarEntry.getName().startsWith(packageName) && jarEntry.getName().endsWith(".class")) {
                            classes.add(jarEntry.getName()
                                .replaceAll("/", "\\.")
                                .replace(".class", ""));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {

            }

        } else {
            URL resource = classLoader.getResource(path);
            File dir = new File(resource.toURI());
            classes.addAll(findClasses(dir, packageName));
        }

        return classes.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @see <a href="https://stackoverflow.com/a/520344">Stackoverflow inspiration</a>
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return fully qualified class name strings
     */
    public static List<String> findClasses(File directory, String packageName) {
        List<String> classes = new ArrayList<String>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        return classes;
    }

    /**
     * Inspired by getClassesInPackage from Fabrication.
     */
    public static Iterable<ClassPath.ClassInfo> getClassesInPackage(ClassLoader classLoader, String packageName) {
        try {
            List<String> classes = getClassesInPackage(packageName);

            Constructor<ClassPath.ClassInfo> cons = ClassPath.ClassInfo.class.getDeclaredConstructor(String.class, ClassLoader.class);
            cons.setAccessible(true);
            List<ClassPath.ClassInfo> rtrn = Lists.newArrayList();

            for (String c : classes) {
                if (c.startsWith(packageName)) {
                    String resource = c.replace('.', '/') + ".class";
                    rtrn.add(cons.newInstance(resource, classLoader));
                }
            }

            return rtrn;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
