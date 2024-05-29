package svenhjol.charm.feature.atlases.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.atlases.Atlases;

import java.util.List;

public final class Providers extends ProviderHolder<Atlases> implements WandererTradeProvider {
    public Providers(Atlases feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getWandererTrades() {
        return List.of(new WandererTrade() {
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
