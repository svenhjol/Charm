package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Specify a list of trade definitions that will be added to the wandering trader's trades.
 * @see WandererTrade
 */
@SuppressWarnings("unused")
public interface WandererTradeProvider {
    /**
     * Trade definitions to be added to the normal (first four) trade slots.
     */
    default List<WandererTrade> getWandererTrades() {
        return List.of();
    }

    /**
     * Trade definitions to be added to the rare (last two) trade slots.
     */
    default List<WandererTrade> getRareWandererTrades() {
        return List.of();
    }
}
