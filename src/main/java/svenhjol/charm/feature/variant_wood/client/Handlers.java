package svenhjol.charm.feature.variant_wood.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charm.feature.variant_wood.VariantWoodClient;
import svenhjol.charm.feature.variant_wood.block.ChestBlock;
import svenhjol.charm.feature.variant_wood.block.entity.ChestBlockEntity;
import svenhjol.charm.feature.variant_wood.block.TrappedChestBlock;
import svenhjol.charm.feature.variant_wood.block.entity.TrappedChestBlockEntity;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.Optional;

public final class Handlers extends FeatureHolder<VariantWoodClient> {
    private final ChestBlockEntity cachedNormalChest;
    private final TrappedChestBlockEntity cachedTrappedChest;

    public Handlers(VariantWoodClient feature) {
        super(feature);

        // Cache the chest block entities for fast lookup by the renderer.
        cachedNormalChest = new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        cachedTrappedChest = new TrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());
    }

    public Optional<BlockEntity> renderChestBlockItem(ItemStack stack, Block block) {
        if (block instanceof ChestBlock chest) {
            cachedNormalChest.setMaterial(chest.getMaterial());
            return Optional.of(cachedNormalChest);

        } else if (block instanceof TrappedChestBlock trappedChest) {
            cachedTrappedChest.setMaterial(trappedChest.getMaterial());
            return Optional.of(cachedTrappedChest);
        }

        return Optional.empty();
    }
}
