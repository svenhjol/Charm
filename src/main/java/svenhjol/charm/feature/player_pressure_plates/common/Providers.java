package svenhjol.charm.feature.player_pressure_plates.common;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.iface.WandererTrade;
import svenhjol.charm.api.iface.WandererTradeProvider;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlates;

import java.util.List;

public final class Providers extends ProviderHolder<PlayerPressurePlates> implements WandererTradeProvider {
    public Providers(PlayerPressurePlates feature) {
        super(feature);
    }

    @Override
    public List<WandererTrade> getWandererTrades() {
        return List.of(new WandererTrade() {
            @Override
            public ItemLike getItem() {
                return feature().registers.block.get();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getCost() {
                return 6;
            }
        });
    }
}