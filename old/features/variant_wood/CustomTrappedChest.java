package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomTrappedChest {
    public static Supplier<BlockEntityType<VariantTrappedChestBlockEntity>> blockEntity;
    public final Supplier<VariantTrappedChestBlock> block;
    public final Supplier<VariantTrappedChestBlock.BlockItem> item;

    public CustomTrappedChest(CommonRegistry registry, IVariantMaterial material) {
        var id = "trapped_" + material.getSerializedName() + "_chest";

        block = registry.block(id, () -> new VariantTrappedChestBlock(material));
        item = registry.item(id, () -> new VariantTrappedChestBlock.BlockItem(block));

        // Trapped chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(blockEntity, List.of(block));
    }
}
