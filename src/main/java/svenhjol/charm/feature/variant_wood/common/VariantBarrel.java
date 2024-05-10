package svenhjol.charm.feature.variant_wood.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.BarrelBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.List;
import java.util.function.Supplier;

public class VariantBarrel implements FeatureResolver<VariantWood> {
    public final Supplier<BarrelBlock> block;
    public final Supplier<BarrelBlock.BlockItem> item;

    public VariantBarrel(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = material.getSerializedName() + "_barrel";

        block = registry.block(id, () -> new BarrelBlock(material));
        item = registry.item(id, () -> new BarrelBlock.BlockItem(block));

        // Barrels can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.BARREL, List.of(block));

        // Add this barrel to the Fisherman's point of interest.
        registry.pointOfInterestBlockStates(
            () -> BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(PoiTypes.FISHERMAN),
            () -> block.get().getStateDefinition().getPossibleStates());
    }

    @Override
    public Class<VariantWood> featureType() {
        return VariantWood.class;
    }
}
