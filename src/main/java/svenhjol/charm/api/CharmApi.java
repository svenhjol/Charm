package svenhjol.charm.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import svenhjol.charm.decoration.module.Crates;

public class CharmApi
{
    public static void addInvalidCrateItem(Class<? extends Item> item)
    {
        Crates.invalidItems.add(item);
    }

    public static void addInvalidCrateBlock(Class<? extends Block> block)
    {
        Crates.invalidBlocks.add(block);
    }
}
