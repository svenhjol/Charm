package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature(mod = Charm.MOD_ID)
public class SmoothGlowstoneClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(SmoothGlowstone.class));
    }

    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry()
            .itemTab(SmoothGlowstone.blockItem, CreativeModeTabs.BUILDING_BLOCKS, Items.AMETHYST_BLOCK);
    }
}
