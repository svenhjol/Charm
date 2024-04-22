package svenhjol.charm.api.iface;

import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public interface IWandererTrade {
    /**
     * The item being sold.
     */
    ItemLike getItem();

    /**
     * The stack size of the item.
     */
    int getCount();

    /**
     * The base cost of the item.
     */
    int getCost();

    /**
     * The number of trades before the trader stops selling this item.
     */
    default int getTrades() {
        return 1;
    }
}
