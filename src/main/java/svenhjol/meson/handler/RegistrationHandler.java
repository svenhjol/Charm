package svenhjol.meson.handler;

import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEffect;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.iface.IMesonPotion;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHandler
{
    public static List<IMesonBlock> BLOCKS = new ArrayList<>();
    public static List<IMesonItem> ITEMS = new ArrayList<>();
    public static List<IMesonPotion> POTIONS = new ArrayList<>();
    public static List<IMesonEffect> EFFECTS = new ArrayList<>();

    public static void addBlock(IMesonBlock block)
    {
        BLOCKS.add(block);
    }

    public static void addEffect(IMesonEffect effect)
    {
        EFFECTS.add(effect);
    }

    public static void addItem(IMesonItem item)
    {
        ITEMS.add(item);
    }

    public static void addPotion(IMesonPotion potion)
    {
        POTIONS.add(potion);
    }
}
