package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.BlockItem;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Smooth glowstone.")
public class SmoothGlowstone extends CharmFeature {
    public static final String ID = "smooth_glowstone";
    public static Supplier<SmoothGlowstoneBlock> block;
    public static Supplier<BlockItem> blockItem;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        block = registry.block(ID, () -> new SmoothGlowstoneBlock(this));
        blockItem = registry.item(ID, () -> new SmoothGlowstoneBlock.BlockItem(this, block));
    }
}
