package svenhjol.charm.base.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public abstract class CharmSlabBlock extends SlabBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSlabBlock(CharmModule module, String name, Settings settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
    }

    public CharmSlabBlock(CharmModule module, String name, MapColor color) {
        this(module, name, Settings.of(Material.WOOD, color)
            .strength(2.0F, 3.0F)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
