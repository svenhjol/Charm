package svenhjol.charm.automation.feature;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.automation.block.VariableRedstoneLampBlock;
import svenhjol.meson.Feature;

public class VariableRedstoneLamp extends Feature
{
    public static VariableRedstoneLampBlock block;
    public static BlockItem blockItem;

    @Override
    public void init()
    {
        super.init();
        block = new VariableRedstoneLampBlock();
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
