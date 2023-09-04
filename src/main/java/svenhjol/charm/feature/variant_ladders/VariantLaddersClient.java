package svenhjol.charm.feature.variant_ladders;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class VariantLaddersClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(VariantLadders.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            // Add ladders to functional tab.
            for (var item : VariantLadders.LADDER_BLOCK_ITEMS.values()) {
                CharmClient.instance().registry().itemTab(
                    item,
                    CreativeModeTabs.FUNCTIONAL_BLOCKS,
                    Items.LADDER
                );
            }
        }
    }

    @Override
    public void runAlways() {
        // Ladders should render transparent areas cut out.
        for (var ladder : VariantLadders.LADDER_BLOCKS.values()) {
            CharmClient.instance().registry().blockRenderType(ladder, RenderType::cutout);
        }
    }
}
