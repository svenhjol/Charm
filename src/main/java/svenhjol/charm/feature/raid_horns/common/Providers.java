package svenhjol.charm.feature.raid_horns.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.raid_horns.RaidHorns;

import java.util.List;

public final class Providers extends ProviderHolder<RaidHorns> implements WandererTradeProvider {
    public final static int WANDERER_TRADE_COST = 30; // Number of emeralds a WT charges.

    public Providers(RaidHorns feature) {
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
                return 1;
            }

            @Override
            public int getCost() {
                return WANDERER_TRADE_COST;
            }
        });
    }
}
