package svenhjol.charm.decoration.feature;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.decoration.block.GoldLanternBlock;
import svenhjol.meson.Feature;

public class GoldLantern extends Feature
{
    public static GoldLanternBlock block;
    public static BlockItem blockItem;

    @Override
    public void init()
    {
        super.init();
        block = new GoldLanternBlock();
        blockItem = block.getBlockItem();
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry)
    {
        registry.register(block);
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry)
    {
        registry.register(blockItem);
    }
}
