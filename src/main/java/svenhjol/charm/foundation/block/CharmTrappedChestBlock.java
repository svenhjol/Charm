package svenhjol.charm.foundation.block;

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
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.VariantMaterialHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWood;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class CharmTrappedChestBlock extends ChestBlock implements VariantMaterialHolder {
    private static final CustomWood CUSTOM_WOOD = Resolve.feature(CustomWood.class);
    private final IVariantMaterial material;

    public CharmTrappedChestBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.TRAPPED_CHEST), CUSTOM_WOOD.registers.trappedChestBlockEntity::get);
        this.material = material;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TrappedChestBlockEntity(pos, state);
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
        return Mth.clamp(TrappedChestBlockEntity.getOpenCount(blockGetter, blockPos), 0, 15);
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
    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<CharmTrappedChestBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}