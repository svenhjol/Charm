package svenhjol.charm.feature.core.custom_wood.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    private CustomChestBlockEntity cachedNormalChest;
    private CustomTrappedChestBlockEntity cachedTrappedChest;

    public Handlers(CustomWoodClient feature) {
        super(feature);
    }

    private void initializeCachedChests() {
        if (cachedNormalChest == null) {
            var linkedRegisters = feature().linked().registers;
            var customChestBlock = linkedRegisters.holders.values().stream()
                    .flatMap(holder -> holder.chest().stream())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No custom chest block found"))
                    .block.get();
            cachedNormalChest = new CustomChestBlockEntity(BlockPos.ZERO, customChestBlock.defaultBlockState());
        }
        if (cachedTrappedChest == null) {
            var linkedRegisters = feature().linked().registers;
            var customTrappedChestBlock = linkedRegisters.holders.values().stream()
                    .flatMap(holder -> holder.trappedChest().stream())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No custom trapped chest block found"))
                    .block.get();
            cachedTrappedChest = new CustomTrappedChestBlockEntity(BlockPos.ZERO, customTrappedChestBlock.defaultBlockState());
        }
    }

    public Optional<BlockEntity> renderChestBlockItem(ItemStack stack, Block block) {
        initializeCachedChests();

        if (block instanceof CustomTrappedChestBlock trappedChest) {
            cachedTrappedChest.setMaterial(trappedChest.getMaterial());
            return Optional.of(cachedTrappedChest);
        } else if (block instanceof CustomChestBlock chest) {
            cachedNormalChest.setMaterial(chest.getMaterial());
            return Optional.of(cachedNormalChest);
        }

        return Optional.empty();
    }

    public void clientEntityJoin(Entity entity, Level level) {
        if (!(entity instanceof Player)) return;
        feature().linked().registers.fixSignItems();
    }
}