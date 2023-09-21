package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmapi.iface.IWandererTrade;
import svenhjol.charmapi.iface.IWandererTradeProvider;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Player-only pressure plates crafted using gilded blackstone.")
public class PlayerPressurePlates extends CharmFeature implements IWandererTradeProvider {
    static final String ID = "player_pressure_plate";
    static Supplier<PlayerPressurePlateBlock> block;
    static Supplier<PlayerPressurePlateBlock.BlockItem> blockItem;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        block = registry.block(ID, () -> new PlayerPressurePlateBlock(this));
        blockItem = registry.item(ID, () -> new PlayerPressurePlateBlock.BlockItem(this, block));
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return block.get();
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
