package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class ArcanePurpurClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ArcanePurpur.class;
    }

    @Override
    public void runWhenEnabled() {
        var registry = mod().registry();

        registry.itemTab(
            ArcanePurpur.arcanePurpurSlab,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            ArcanePurpur.arcanePurpurStairs,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            ArcanePurpur.arcanePurpurBlock,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
    }
}
