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
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.TWEAKS, hasSubscriptions = true)
public class PickaxesBreakPistons extends Feature
{
    public static Block block;
    public static Item blockItem;
    public static final ResourceLocation ID = new ResourceLocation("piston");

    @Override
    public void init()
    {
        block = new PistonBlock(false, Block.Properties
            .create(Material.PISTON)
            .harvestTool(ToolType.PICKAXE)
            .hardnessAndResistance(0.5F));

        block.setRegistryName(ID);
        blockItem = new BlockItem(block, (new Item.Properties()).group(ItemGroup.REDSTONE)).setRegistryName(ID);

        // Vanilla registry
        Registry.register(Registry.BLOCK, ID, block);
        Registry.register(Registry.ITEM, ID, blockItem);

        // Forge registry
        RegistryHandler.registerBlock(block, block.getRegistryName());
        RegistryHandler.registerItem(blockItem, block.getRegistryName());
    }
}
