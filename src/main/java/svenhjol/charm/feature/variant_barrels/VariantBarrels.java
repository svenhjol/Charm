package svenhjol.charm.feature.variant_barrels;

import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrelBlock.BlockItem;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, priority = 10, canBeDisabled = false, description = "Registers variant barrels.")
public class VariantBarrels extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantBarrelBlock>> BARREL_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<BlockItem>> BARREL_BLOCK_ITEMS = new HashMap<>();

    @Override
    public void register() {
        ApiHelper.addConsumer(IVariantBarrelProvider.class,
            provider -> provider.getVariantBarrels().forEach(this::registerBarrel));
    }

    private void registerBarrel(IVariantMaterial material) {
        var registry = Charm.instance().registry();
        var id = material.getSerializedName() + "_barrel";

        var block = registry.block(id, () -> new VariantBarrelBlock(material));
        var blockItem = registry.item(id, () -> new BlockItem(block));

        BARREL_BLOCKS.put(material, block);
        BARREL_BLOCK_ITEMS.put(material, blockItem);

        registry.fuel(blockItem);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.BARREL, List.of(block));

        // Add this barrel to the Fisherman's point of interest.
        registry.pointOfInterestBlockStates(() -> PoiTypes.FISHERMAN, () -> block.get().getStateDefinition().getPossibleStates());
    }
}
