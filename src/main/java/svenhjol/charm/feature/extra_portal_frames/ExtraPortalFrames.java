package svenhjol.charm.feature.extra_portal_frames;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.ConfigHelper;

import java.util.List;
import java.util.function.BooleanSupplier;

@Feature(mod = Charm.MOD_ID, description = "Adds more blocks that can be used to build nether portal frames.\n" +
    "By default this adds Crying Obsidian.")
public class ExtraPortalFrames extends CharmFeature {
    public static TagKey<Block> NETHER_PORTAL_FRAMES;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("immersiveportals"));
    }

    @Override
    public void register() {
        NETHER_PORTAL_FRAMES = TagKey.create(BuiltInRegistries.BLOCK.key(), Charm.instance().makeId("nether_portal_frames"));
    }

    @Override
    public void runWhenEnabled() {
        PortalShape.FRAME = (blockState, blockView, blockPos) -> isValidBlockState(blockState);
    }

    public static boolean isValidBlockState(BlockState state) {
        return state.is(NETHER_PORTAL_FRAMES);
    }
}
