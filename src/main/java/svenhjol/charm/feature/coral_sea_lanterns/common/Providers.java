package svenhjol.charm.feature.coral_sea_lanterns.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanterns;
import svenhjol.charm.charmony.feature.ProviderHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class Providers extends ProviderHolder<CoralSeaLanterns> implements WandererTradeProvider {
    public Providers(CoralSeaLanterns feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getWandererTrades() {
        List<WandererTrade> out = new ArrayList<>();

        for (Supplier<Block> block : feature().registers.blocks.values()) {
            out.add(new WandererTrade() {
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
