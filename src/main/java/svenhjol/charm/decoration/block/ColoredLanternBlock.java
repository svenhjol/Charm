package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;

public class ColoredLanternBlock extends BaseLanternBlock {
    public ColoredLanternBlock(MesonModule module, ColorVariant color) {
        super(module, color.getName() + "_lantern", Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(3.5F)
            .sound(SoundType.LANTERN)
            .lightValue(15));
    }
}
