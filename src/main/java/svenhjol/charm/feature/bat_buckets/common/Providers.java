package svenhjol.charm.feature.bat_buckets.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<BatBuckets> implements IWandererTradeProvider {
    public Providers(BatBuckets feature) {
        super(feature);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
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
