package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.crafting.module.SmoothGlowstone;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

public class SmoothGlowstoneBlock extends MesonBlock
{
    public SmoothGlowstoneBlock(MesonModule module)
    {
        super(module, "smooth_glowstone", Block.Properties
            .create(Material.GLASS)
            .lightValue(15)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(SmoothGlowstone.hardness)
        );
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }
}
