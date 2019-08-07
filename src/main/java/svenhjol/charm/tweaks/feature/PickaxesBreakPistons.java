package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.RegistrationHandler;

public class PickaxesBreakPistons extends Feature
{
    @Override
    public void init()
    {
        super.init();

        PistonBlock block = new PistonBlock(false, Block.Properties
            .create(Material.PISTON)
            .harvestTool(ToolType.PICKAXE)
            .hardnessAndResistance(0.5F));

        BlockItem item = new BlockItem(block, (new Item.Properties()).group(ItemGroup.REDSTONE));

        RegistrationHandler.addBlockOverride("piston", block, item);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
