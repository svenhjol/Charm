package svenhjol.charm.feature.variant_ladders;

import svenhjol.charm.Charm;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant ladders.")
public class VariantLadders extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock>> LADDER_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock.BlockItem>> LADDER_BLOCK_ITEMS = new HashMap<>();

    public static void registerLadder(ICommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_ladder";

        var block = registry.block(id, () -> new VariantLadderBlock(material));
        var blockItem = registry.item(id, () -> new VariantLadderBlock.BlockItem(block));

        LADDER_BLOCKS.put(material, block);
        LADDER_BLOCK_ITEMS.put(material, blockItem);
    }
}
