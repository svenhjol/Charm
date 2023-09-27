package svenhjol.charm.feature.variant_wood.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.variant_wood.block.VariantBarrelBlock;
import svenhjol.charmony_api.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomBarrel {
    public final Supplier<VariantBarrelBlock> block;
    public final Supplier<VariantBarrelBlock.BlockItem> item;

    public CustomBarrel(ICommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_barrel";

        block = registry.block(id, () -> new VariantBarrelBlock(material));
        item = registry.item(id, () -> new VariantBarrelBlock.BlockItem(block));

        // Barrels can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.BARREL, List.of(block));

        // Add this barrel to the Fisherman's point of interest.
        registry.pointOfInterestBlockStates(() -> BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(PoiTypes.FISHERMAN), () -> block.get().getStateDefinition().getPossibleStates());
    }
}
