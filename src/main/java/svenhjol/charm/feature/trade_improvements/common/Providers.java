package svenhjol.charm.feature.trade_improvements.common;

import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.trade_improvements.TradeImprovements;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;
import svenhjol.charm.foundation.villages.GenericTrades;

public final class Providers extends ProviderHolder<TradeImprovements> implements IWandererTradeProvider {
    public Providers(TradeImprovements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        if (feature().charmModItems()) {
            var registry = feature().registry();

            ApiHelper.consume(IWandererTradeProvider.class,
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
