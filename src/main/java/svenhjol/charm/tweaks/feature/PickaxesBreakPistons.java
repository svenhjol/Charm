package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.Feature;

public class PickaxesBreakPistons extends Feature
{
    public static Block block;
    public static Item blockItem;
    public static final ResourceLocation ID = new ResourceLocation("piston");

    @Override
    public void init()
    {
        super.init();

        block = new PistonBlock(false, Block.Properties
            .create(Material.PISTON)
            .harvestTool(ToolType.PICKAXE)
            .hardnessAndResistance(0.5F));
        block.setRegistryName(ID);

        blockItem = new BlockItem(block, (new Item.Properties()).group(ItemGroup.REDSTONE)).setRegistryName(ID);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    @Override
    public void registerBlocks(IForgeRegistry<Block> registry)
    {
        Registry.register(Registry.BLOCK, ID, block); // Vanilla registry
    }

    @Override
    public void registerItems(IForgeRegistry<Item> registry)
    {
        Registry.register(Registry.ITEM, ID, blockItem); // Vanilla registry
    }
}
