package svenhjol.charm.base.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.WallSignBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import svenhjol.charm.base.CharmModule;

public abstract class CharmWallSignBlock extends WallSignBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmWallSignBlock(CharmModule module, String name, SignType signType, Settings settings) {
        super(settings, signType);

        this.register(module, name);
        this.module = module;
    }

    public CharmWallSignBlock(CharmModule module, String name, CharmSignBlock block, SignType signType, MapColor color) {
        this(module, name, signType, Settings.of(Material.WOOD, color)
            .noCollision()
            .strength(1.0F)
            .sounds(BlockSoundGroup.WOOD)
            .dropsLike(block));
    }

    @Override
    public void createBlockItem(Identifier id) {
        // no, because infinite loop
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
