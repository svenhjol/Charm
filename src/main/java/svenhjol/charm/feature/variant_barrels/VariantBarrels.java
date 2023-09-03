package svenhjol.charm.feature.variant_barrels;

import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrelBlock.BlockItem;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.iface.ICommonRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant barrels.")
public class VariantBarrels extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantBarrelBlock>> BARREL_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<BlockItem>> BARREL_BLOCK_ITEMS = new HashMap<>();

    public static void registerBarrel(ICommonRegistry registry, IVariantMaterial material) {
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