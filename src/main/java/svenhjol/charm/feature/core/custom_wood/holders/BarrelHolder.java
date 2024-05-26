package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomBarrelBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.List;
import java.util.function.Supplier;

public class BarrelHolder {
    public final Supplier<CustomBarrelBlock> block;
    public final Supplier<CustomBarrelBlock.BlockItem> item;

    public BarrelHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_barrel";

        block = registry.block(id, () -> new CustomBarrelBlock(material));
        item = registry.item(id, () -> new CustomBarrelBlock.BlockItem(block));

        // Barrels can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.BARREL, List.of(block));

        // Add this barrel to the Fisherman's point of interest.
        registry.pointOfInterestBlockStates(
            () -> BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(PoiTypes.FISHERMAN),
            () -> block.get().getStateDefinition().getPossibleStates());

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.BARREL, item);
    }
}
