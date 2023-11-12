package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.ItemLike;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.IWandererTrade;
import svenhjol.charmony_api.iface.IWandererTradeProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CoralSeaLanterns extends CommonFeature implements IWandererTradeProvider {
    public static final Map<CoralMaterial, Supplier<CoralSeaLanternBlock>> BLOCKS = new LinkedHashMap<>();
    public static final Map<CoralMaterial, Supplier<CoralSeaLanternBlock.BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();

    @Override
    public String description() {
        return "Coral can be combined with sea lanterns to make colored variants.";
    }

    @Override
    public void register() {
        var registry = mod().registry();

        for (var material : CoralMaterial.values()) {
            var id = material.getSerializedName() + "_sea_lantern";
            var block = registry.block(id, () -> new CoralSeaLanternBlock(material));
            var blockItem = registry.item(id, () -> new CoralSeaLanternBlock.BlockItem(block));
            BLOCKS.put(material, block);
            BLOCK_ITEMS.put(material, blockItem);
        }

        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        List<IWandererTrade> out = new ArrayList<>();

        for (Supplier<CoralSeaLanternBlock> block : BLOCKS.values()) {
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
