package svenhjol.charm.feature.block_of_ender_pearls;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BlockOfEnderPearlsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(BlockOfEnderPearls.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.instance().registry().itemTab(
                BlockOfEnderPearls.BLOCK_ITEM,
                CreativeModeTabs.FUNCTIONAL_BLOCKS,
                Items.ENDER_EYE
            );
        }
    }
}
