package svenhjol.meson.helper;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigHelper
{
    public static final String TRANSFORMERS = "Transformers";
    public static final String MODULES = "@Modules";

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

    public static void saveIfChanged(Configuration config)
    {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
