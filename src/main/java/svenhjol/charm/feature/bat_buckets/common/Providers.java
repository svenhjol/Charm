package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.bat_buckets.BatBuckets;

import java.util.List;

public final class Providers extends ProviderHolder<BatBuckets> implements WandererTradeProvider {
    public Providers(BatBuckets feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getWandererTrades() {
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
                return 8;
            }
        });
    }
}
