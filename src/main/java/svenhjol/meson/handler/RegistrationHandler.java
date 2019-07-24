package svenhjol.meson.handler;

import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonItem;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHandler
{
    public static List<IMesonItem> ITEMS = new ArrayList<>();
    public static List<IMesonBlock> BLOCKS = new ArrayList<>();

    public static void addBlock(IMesonBlock block)
    {
        BLOCKS.add(block);
    }

    public static void addItem(IMesonItem item)
    {
        ITEMS.add(item);
    }
}
