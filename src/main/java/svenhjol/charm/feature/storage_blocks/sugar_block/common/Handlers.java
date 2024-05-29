package svenhjol.charm.feature.storage_blocks.sugar_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.enums.EventResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlock;

public final class Handlers extends FeatureHolder<SugarBlock> {
    public Handlers(SugarBlock feature) {
        super(feature);
    }

    public EventResult sugarDissolve(Level level, BlockPos pos) {
        level.removeBlock(pos, true);
        level.playSound(null, pos, feature().registers.dissolveSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        return EventResult.PASS;
    }
}
