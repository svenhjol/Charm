package svenhjol.charm.base.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public abstract class CharmPlanksBlock extends CharmBlock {
    public CharmPlanksBlock(CharmModule module, String name, Settings settings) {
        super(module, name, settings);
    }

    public CharmPlanksBlock(CharmModule module, String name, MapColor color) {
        this(module, name, Settings.of(Material.WOOD, color)
            .strength(2.0F, 3.0F)
            .sounds(BlockSoundGroup.WOOD));
    }
}
