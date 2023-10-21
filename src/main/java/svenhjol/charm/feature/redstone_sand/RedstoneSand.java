package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;

import java.util.List;
import java.util.function.Supplier;

public class RedstoneSand extends CommonFeature implements IWandererTradeProvider {
    private static final String ID = "redstone_sand";
    static Supplier<Block> block;
    static Supplier<Item> blockItem;

    @Override
    public String description() {
        return "A block that acts like sand but is powered like a block of redstone.";
    }

    @Override
    public void register() {
        var registry = mod().registry();
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
