package svenhjol.charm.feature.variant_wood.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.variant_wood.block.VariantTrappedChestBlock;
import svenhjol.charm.feature.variant_wood.entity.VariantTrappedChestBlockEntity;
import svenhjol.charmony_api.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomTrappedChest {
    public static Supplier<BlockEntityType<VariantTrappedChestBlockEntity>> blockEntity;
    public final Supplier<VariantTrappedChestBlock> block;
    public final Supplier<VariantTrappedChestBlock.BlockItem> item;

    public CustomTrappedChest(ICommonRegistry registry, IVariantMaterial material) {
        if (blockEntity == null) {
            blockEntity = registry.blockEntity("variant_trapped_chest", () -> VariantTrappedChestBlockEntity::new);
        }

        var id = "trapped_" + material.getSerializedName() + "_chest";

        block = registry.block(id, () -> new VariantTrappedChestBlock(material));
        item = registry.item(id, () -> new VariantTrappedChestBlock.BlockItem(block));

        // Trapped chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(blockEntity, List.of(block));
    }
}
