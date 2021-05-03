package svenhjol.charm.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import svenhjol.charm.base.CharmModule;

public abstract class CharmStairsBlock extends StairsBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmStairsBlock(CharmModule module, String name, BlockState state, Settings settings) {
        super(state, settings);

        this.register(module, name);
        this.module = module;
    }

    public CharmStairsBlock(CharmModule module, String name, Block block) {
        this(module, name, block.getDefaultState(), Settings.copy(block));
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
