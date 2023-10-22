package svenhjol.charm.feature.extra_portal_frames;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ExtraPortalFrames extends CommonFeature {
    @Override
    public String description() {
        return """
            Adds more blocks that can be used to build nether portal frames.
            By default this adds Crying Obsidian.""";
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("immersiveportals"));
    }

    @Override
    public void runWhenEnabled() {
        PortalShape.FRAME = (blockState, blockView, blockPos) -> isValidBlockState(blockState);
    }

    public static boolean isValidBlockState(BlockState state) {
        return state.is(CharmTags.NETHER_PORTAL_FRAMES);
    }
}
