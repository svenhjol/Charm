package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "A block that acts like sand but is powered like a block of redstone.")
public class RedstoneSand extends CharmFeature implements IWandererTradeProvider {
    private static final String ID = "redstone_sand";
    static Supplier<Block> block;
    static Supplier<Item> blockItem;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        block = registry.block(ID, RedstoneSandBlock::new);
        blockItem = registry.item(ID, RedstoneSandBlock.BlockItem::new);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return blockItem.get();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getCost() {
                return 1;
            }
        });
    }
}
