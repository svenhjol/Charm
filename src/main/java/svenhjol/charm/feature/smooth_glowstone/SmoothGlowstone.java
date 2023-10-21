package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.BlockItem;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class SmoothGlowstone extends CommonFeature {
    public static final String ID = "smooth_glowstone";
    public static Supplier<SmoothGlowstoneBlock> block;
    public static Supplier<BlockItem> blockItem;

    @Override
    public String description() {
        return "Smooth glowstone.";
    }

    @Override
    public void register() {
        var registry = mod().registry();
        block = registry.block(ID, () -> new SmoothGlowstoneBlock(this));
        blockItem = registry.item(ID, () -> new SmoothGlowstoneBlock.BlockItem(this, block));
    }
}
