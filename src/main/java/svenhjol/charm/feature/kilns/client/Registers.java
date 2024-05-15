package svenhjol.charm.feature.kilns.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.kilns.KilnsClient;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<KilnsClient> {
    public Registers(KilnsClient feature) {
        super(feature);
        var registry = feature().registry();
        var common = feature().common();

        // Early registration of enums.
        registry.recipeBookCategoryEnum("kiln_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("kiln", common.registers.block);

        registry.menuScreen(common.registers.menu, () -> Screen::new);
        registry.recipeBookCategory("kiln",
            Resolve.feature(Firing.class).registers.recipeType,
            common.registers.recipeBookType);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.blockItem,
            CreativeModeTabs.FUNCTIONAL_BLOCKS,
            Items.SMOKER
        );
    }
}
