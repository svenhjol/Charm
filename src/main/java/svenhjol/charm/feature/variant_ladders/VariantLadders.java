package svenhjol.charm.feature.variant_ladders;

import svenhjol.charm.Charm;
import svenhjol.charm.api.IVariantLadderProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant ladders.")
public class VariantLadders extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock>> LADDER_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<VariantLadderBlock.BlockItem>> LADDER_BLOCK_ITEMS = new HashMap<>();

    @Override
    public void register() {
        ApiHelper.consume(IVariantLadderProvider.class,
            provider -> provider.getVariantLadders().forEach(this::registerLadder));
    }

    private void registerLadder(IVariantMaterial material) {
        var registry = Charm.instance().registry();
        var id = material.getSerializedName() + "_ladder";

        var block = registry.block(id, () -> new VariantLadderBlock(material));
        var blockItem = registry.item(id, () -> new VariantLadderBlock.BlockItem(block));

        LADDER_BLOCKS.put(material, block);
        LADDER_BLOCK_ITEMS.put(material, blockItem);
    }
}
