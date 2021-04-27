package svenhjol.charm.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;

public abstract class CharmDoorBlock extends DoorBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmDoorBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmDoorBlock(CharmModule module, String name, Block block) {
        this(module, name, Settings.of(Material.WOOD, block.getDefaultMapColor())
            .strength(3.0F)
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque());
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
