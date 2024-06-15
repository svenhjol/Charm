package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.FuelProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomBarrelBlock extends BarrelBlock {
    private final CustomMaterial material;

    public CustomBarrelBlock(CustomMaterial material) {
        super(Properties.copy(Blocks.BARREL));
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

    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements FuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CustomBarrelBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
