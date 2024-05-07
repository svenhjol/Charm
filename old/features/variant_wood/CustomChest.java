package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomChest {
    public static Supplier<BlockEntityType<VariantChestBlockEntity>> blockEntity;
    public final Supplier<VariantChestBlock> block;
    public final Supplier<VariantChestBlock.BlockItem> item;
    public final String modId;

    public CustomChest(CommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_chest";

        modId = registry.id();
        block = registry.block(id, () -> new VariantChestBlock(material));
        item = registry.item(id, () -> new VariantChestBlock.BlockItem(block));

        // Chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(blockEntity, List.of(block));
    }
}
