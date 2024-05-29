package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.FuelProvider;
import svenhjol.charm.charmony.iface.VariantMaterialHolder;

import java.util.function.Supplier;

public class CustomLadderBlock extends LadderBlock implements VariantMaterialHolder {
    private final CustomMaterial material;

    public CustomLadderBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.LADDER));
        this.material = material;
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements FuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CustomLadderBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
