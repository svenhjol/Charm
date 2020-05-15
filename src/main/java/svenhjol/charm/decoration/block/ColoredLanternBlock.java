package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import svenhjol.meson.MesonModule;

public class ColoredLanternBlock extends BaseLanternBlock {
    public ColoredLanternBlock(MesonModule module, DyeColor color) {
        super(module, color + "_lantern", Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(3.5F)
            .sound(SoundType.LANTERN)
            .lightValue(15));
    }
}
