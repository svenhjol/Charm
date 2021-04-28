package svenhjol.charm.base.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public class CharmTrapdoorBlock extends TrapdoorBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmTrapdoorBlock(CharmModule module, String name, Settings settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
    }

    public CharmTrapdoorBlock(CharmModule module, String name, MapColor color) {
        this(module, name, Settings.of(Material.WOOD, color)
            .strength(3.0F)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
