package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class DataProviders extends FeatureHolder<Atlases> implements IWandererTradeProvider {
    public DataProviders(Atlases feature) {
        super(feature);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return feature().registers.item.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 5;
            }
        });
    }
}
