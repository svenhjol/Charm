package svenhjol.charm.feature.variant_wood.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.variant_wood.block.VariantChestBlock;
import svenhjol.charm.feature.variant_wood.entity.VariantChestBlockEntity;
import svenhjol.charmapi.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomChest {
    public static Supplier<BlockEntityType<VariantChestBlockEntity>> blockEntity;
    public final Supplier<VariantChestBlock> block;
    public final Supplier<VariantChestBlock.BlockItem> item;

    public CustomChest(ICommonRegistry registry, IVariantMaterial material) {
        if (blockEntity == null) {
            blockEntity = registry.blockEntity("variant_chest", () -> VariantChestBlockEntity::new);
        }

        var id = material.getSerializedName() + "_chest";

        block = registry.block(id, () -> new VariantChestBlock(material));
        item = registry.item(id, () -> new VariantChestBlock.BlockItem(block));

        // Chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(blockEntity, List.of(block));
    }
}
