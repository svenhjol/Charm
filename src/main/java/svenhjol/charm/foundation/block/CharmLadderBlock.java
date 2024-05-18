package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.CustomMaterial;
import svenhjol.charm.api.iface.VariantMaterialHolder;

import java.util.function.Supplier;

public class CharmLadderBlock extends LadderBlock implements VariantMaterialHolder {
    private final CustomMaterial material;

    public CharmLadderBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.LADDER));
        this.material = material;
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CharmLadderBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
