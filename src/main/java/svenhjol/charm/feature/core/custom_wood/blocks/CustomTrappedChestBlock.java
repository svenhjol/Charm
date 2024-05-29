package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.api.iface.FuelProvider;
import svenhjol.charm.api.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.feature.core.custom_wood.blocks.entity.CustomTrappedChestBlockEntity;
import svenhjol.charm.charmony.Resolve;

import java.util.function.Supplier;

public class CustomTrappedChestBlock extends ChestBlock implements VariantMaterialHolder {
    private static final CustomWood CUSTOM_WOOD = Resolve.feature(CustomWood.class);
    private final CustomMaterial material;

    public CustomTrappedChestBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.TRAPPED_CHEST), CUSTOM_WOOD.registers.trappedChestBlockEntity::get);
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Note to self: this was set to vanilla TrappedChestBlockEntity, not the custom override.
        // It didn't render any textures or function properly, but equally showed no useful errors.
        return new CustomTrappedChestBlockEntity(pos, state);
    }

    @Override
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
    }

    /**
     * Vanilla flagged as deprecated
     */
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    /**
     * Vanilla flagged as deprecated
     */
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return Mth.clamp(CustomTrappedChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
    }

    /**
     * Vanilla flagged as deprecated
     */
    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        if (direction == Direction.UP) {
            return blockState.getSignal(blockGetter, blockPos, direction);
        }
        return 0;
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements FuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CustomTrappedChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}