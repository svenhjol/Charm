package svenhjol.charm.feature.core.custom_wood.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charm.feature.core.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomChestBlock;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomTrappedChestBlock;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomChestBlockEntity;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomTrappedChestBlockEntity;
import svenhjol.charm.charmony.feature.FeatureHolder;

import java.util.Optional;

public final class Handlers extends FeatureHolder<CustomWoodClient> {
    private final CustomChestBlockEntity cachedNormalChest;
    private final CustomTrappedChestBlockEntity cachedTrappedChest;

    public Handlers(CustomWoodClient feature) {
        super(feature);

        // Cache the chest block entities for fast lookup by the renderer.
        cachedNormalChest = new CustomChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        cachedTrappedChest = new CustomTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());
    }

    public Optional<BlockEntity> renderChestBlockItem(ItemStack stack, Block block) {
        if (block instanceof CustomTrappedChestBlock trappedChest) {
            cachedTrappedChest.setMaterial(trappedChest.getMaterial());
            return Optional.of(cachedTrappedChest);
        } else if (block instanceof CustomChestBlock chest) {
            cachedNormalChest.setMaterial(chest.getMaterial());
            return Optional.of(cachedNormalChest);
        }

        return Optional.empty();
    }
}
