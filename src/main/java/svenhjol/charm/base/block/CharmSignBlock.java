package svenhjol.charm.base.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.SignBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.SignType;
import svenhjol.charm.base.CharmModule;

public abstract class CharmSignBlock extends SignBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSignBlock(CharmModule module, String name, SignType signType, Settings settings) {
        super(settings, signType);

        this.register(module, name);
        this.module = module;
    }

    public CharmSignBlock(CharmModule module, String name, SignType signType, MapColor color) {
        this(module, name, signType, Settings.of(Material.WOOD, color)
            .noCollision()
            .strength(1.0F)
            .sounds(BlockSoundGroup.WOOD));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
