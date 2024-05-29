package svenhjol.charm.feature.redstone_sand.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.redstone_sand.RedstoneSand;

import java.util.List;

public final class Providers extends ProviderHolder<RedstoneSand> implements WandererTradeProvider {
    public Providers(RedstoneSand feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getWandererTrades() {
        return List.of(new WandererTrade() {
            @Override
            public ItemLike getItem() {
                return feature().registers.blockItem.get();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getCost() {
                return 1;
            }
        });
    }
}
