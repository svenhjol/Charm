package svenhjol.charm.feature.coral_sea_lanterns.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class Providers extends ProviderHolder<CoralSeaLanterns> implements IWandererTradeProvider {
    public Providers(CoralSeaLanterns feature) {
        super(feature);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        List<IWandererTrade> out = new ArrayList<>();

        for (Supplier<Block> block : feature().registers.blocks.values()) {
            out.add(new IWandererTrade() {
                @Override
                public ItemLike getItem() {
                    return block.get();
                }

                @Override
                public int getCount() {
                    return 1;
                }

                @Override
                public int getCost() {
                    return 10;
                }
            });
        }

        return out;
    }
}
