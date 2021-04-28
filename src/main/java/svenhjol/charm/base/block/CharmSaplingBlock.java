package svenhjol.charm.base.block;

import net.minecraft.block.Material;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public abstract class CharmSaplingBlock extends SaplingBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSaplingBlock(CharmModule module, String name, SaplingGenerator generator, Settings settings) {
        super(generator, settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmSaplingBlock(CharmModule module, String name, SaplingGenerator generator) {
        this(module, name, generator, Settings.of(Material.PLANT)
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
