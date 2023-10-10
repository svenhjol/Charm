package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charmony_api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.iface.IFuelProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class VariantBarrelBlock extends BarrelBlock {
    private final IVariantMaterial material;

    public VariantBarrelBlock(IVariantMaterial material) {
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

    public IVariantMaterial getMaterial() {
        return material;
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
    }

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBarrelBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantBarrels;
        }
    }
}
