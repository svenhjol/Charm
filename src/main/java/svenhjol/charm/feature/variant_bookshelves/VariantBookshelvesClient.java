package svenhjol.charm.feature.variant_bookshelves;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class VariantBookshelvesClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(VariantBookshelves.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            for (var item : VariantBookshelves.BOOKSHELF_BLOCK_ITEMS.values()) {
                CharmClient.instance().registry().itemTab(
                    item,
                    CreativeModeTabs.FUNCTIONAL_BLOCKS,
                    Items.BOOKSHELF
                );
            }
        }
    }
}
