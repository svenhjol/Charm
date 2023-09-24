package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IWandererTrade;
import svenhjol.charmony.api.iface.IWandererTradeProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Coral can be combined with sea lanterns to make colored variants.")
public class CoralSeaLanterns extends CharmFeature implements IWandererTradeProvider {
    public static final Map<CoralMaterial, Supplier<CoralSeaLanternBlock>> BLOCKS = new HashMap<>();
    public static final Map<CoralMaterial, Supplier<CoralSeaLanternBlock.BlockItem>> BLOCK_ITEMS = new HashMap<>();

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        for (var material : CoralMaterial.values()) {
            var id = material.getSerializedName() + "_sea_lantern";
            var block = registry.block(id, () -> new CoralSeaLanternBlock(this, material));
            var blockItem = registry.item(id, () -> new CoralSeaLanternBlock.BlockItem(this, block));
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
