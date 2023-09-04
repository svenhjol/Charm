package svenhjol.charm.feature.variant_barrels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.iface.IFuelProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class VariantBarrelBlock extends BarrelBlock {
    private final CharmFeature feature;
    private final IVariantMaterial variantMaterial;

    public VariantBarrelBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.BARREL));
        this.feature = getParent();
        this.variantMaterial = material;

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
        return variantMaterial;
    }

    private static VariantBarrels getParent() {
        return Charm.instance().loader().get(VariantBarrels.class).orElseThrow();
    }

    public static class BlockItem extends CharmBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBarrelBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
