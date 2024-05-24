package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EndermitePowder> implements WandererTradeProvider {
    public Providers(EndermitePowder feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getRareWandererTrades() {
        return List.of(new WandererTrade() {
            @Override
            public ItemLike getItem() {
                return feature().registers.item.get();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getCost() {
                return 20;
            }
        });
    }
}
