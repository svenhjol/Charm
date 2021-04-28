package svenhjol.charm.base.block;

import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public abstract class CharmLeavesBlock extends LeavesBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmLeavesBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmLeavesBlock(CharmModule module, String name) {
        this(module, name, Settings.of(Material.LEAVES)
            .strength(0.2F)
            .ticksRandomly()
            .sounds(BlockSoundGroup.GRASS)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
