package svenhjol.charm.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import svenhjol.charm.crafting.feature.Crate;

public class CharmApi
{
    public static void addInvalidCrateItem(Class<? extends Item> item)
    {
        Crate.invalidItems.add(item);
    }

    public static void addInvalidCrateBlock(Class<? extends Block> block)
    {
        Crate.invalidBlocks.add(block);
    }
}
