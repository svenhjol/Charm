package svenhjol.charm.feature.variant_wood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class VariantBarrelBlock extends BarrelBlock {
    private final IVariantMaterial material;

    public VariantBarrelBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.BARREL));
        this.material = material;

        this.registerDefaultState(this.getStateDefinition()
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(OPEN, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBarrelBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
