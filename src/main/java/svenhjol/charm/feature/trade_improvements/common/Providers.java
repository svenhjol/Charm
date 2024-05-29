package svenhjol.charm.feature.trade_improvements.common;

import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.feature.trade_improvements.TradeImprovements;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.charmony.Api;
import svenhjol.charm.charmony.common.villages.GenericTrades;

public final class Providers extends ProviderHolder<TradeImprovements> implements WandererTradeProvider {
    public Providers(TradeImprovements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        if (feature().charmModItems()) {
            var registry = feature().registry();

            Api.consume(WandererTradeProvider.class,
                provider -> {
                    provider.getWandererTrades().forEach(
                        trade -> registry.wandererTrade(
                            () -> new GenericTrades.ItemsForEmeralds(trade.getItem(),
                                trade.getCost(),
                                trade.getCount(),
                                0,
                                1),
                            false));

                    provider.getRareWandererTrades().forEach(
                        trade -> registry.wandererTrade(
                            () -> new GenericTrades.ItemsForEmeralds(trade.getItem(),
                                trade.getCost(),
                                trade.getCount(),
                                0,
                                1),
                            true));
                });
        }
    }
}
