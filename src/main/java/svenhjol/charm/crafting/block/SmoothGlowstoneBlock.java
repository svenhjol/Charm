package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.SmoothGlowstone;
import svenhjol.meson.MesonBlock;

public class SmoothGlowstoneBlock extends MesonBlock
{
    public SmoothGlowstoneBlock()
    {
        super(Block.Properties
            .create(Material.GLASS)
            .lightValue(15)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(SmoothGlowstone.hardness)
        );

        register(new ResourceLocation(Charm.MOD_ID, "smooth_glowstone"));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }
}
