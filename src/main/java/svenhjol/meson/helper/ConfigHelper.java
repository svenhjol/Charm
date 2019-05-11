package svenhjol.meson.helper;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ConfigHelper
{
    public static int propInt(Configuration config, String name, String category, String comment, int def)
    {
        Property prop = config.get(category, name, def);
        if (!comment.isEmpty()) prop.setComment(comment);
        return prop.getInt(def);
    }

    public static double propDouble(Configuration config, String name, String category, String comment, double def)
    {
        Property prop = config.get(category, name, def);
        if (!comment.isEmpty()) prop.setComment(comment);
        return prop.getDouble(def);
    }

    public static boolean propBoolean(Configuration config, String name, String category, String comment, boolean def)
    {
        Property prop = config.get(category, name, def);
        if (!comment.isEmpty()) prop.setComment(comment);
        return prop.getBoolean(def);
    }

    public static String propString(Configuration config, String name, String category, String comment, String def)
    {
        Property prop = config.get(category, name, def);
        if (!comment.isEmpty()) prop.setComment(comment);
        return prop.getString();
    }

    public static String[] propStringList(Configuration config, String name, String category, String comment, String[] def)
    {
        Property prop = config.get(category, name, def);
        if (!comment.isEmpty()) prop.setComment(comment);
        return prop.getStringList();
    }

    public static boolean checkMods(String ...mods)
    {
        boolean modsLoaded = true;
        for (String mod : mods) {
            modsLoaded = modsLoaded && Loader.isModLoaded(mod);
        }
        return modsLoaded;
    }

    public static boolean checkTransformers(Configuration config, String ...transformers)
    {
        boolean loaded = true;
        for (String transformer : transformers) {
            loaded = loaded && propBoolean(config, transformer, "Transformers", "", true);
        }
        return loaded;
    }

    public static void saveChanges(Configuration config)
    {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
