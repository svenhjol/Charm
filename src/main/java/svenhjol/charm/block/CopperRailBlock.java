package svenhjol.charm.block;

import net.minecraft.block.Material;
import net.minecraft.block.RailBlock;
import net.minecraft.sound.BlockSoundGroup;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;

public class CopperRailBlock extends RailBlock implements ICharmBlock {
    private final CharmModule module;

    public CopperRailBlock(CharmModule module) {
        super(Settings.of(Material.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroup.METAL));
        this.module = module;
        this.register(module, "copper_rail");
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
