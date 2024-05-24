package svenhjol.charm.feature.coral_squids.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<CoralSquids> implements WandererTradeProvider {
    public Providers(CoralSquids feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getRareWandererTrades() {
        return List.of(new WandererTrade() {
            @Override
            public ItemLike getItem() {
                return feature().registers.bucketItem.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 12;
            }
        });
    }
}
