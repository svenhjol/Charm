package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EndermitePowder> implements IWandererTradeProvider {
    public Providers(EndermitePowder feature) {
        super(feature);
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of(new IWandererTrade() {
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
