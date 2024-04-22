package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Specify a list of trade definitions that will be added to the wandering trader's trades.
 * @see IWandererTrade
 */
@SuppressWarnings("unused")
public interface IWandererTradeProvider {
    /**
     * Trade definitions to be added to the normal (first four) trade slots.
     */
    default List<IWandererTrade> getWandererTrades() {
        return List.of();
    }

    /**
     * Trade definitions to be added to the rare (last two) trade slots.
     */
    default List<IWandererTrade> getRareWandererTrades() {
        return List.of();
    }
}
