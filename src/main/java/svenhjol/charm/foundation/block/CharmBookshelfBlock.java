package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmBookshelfBlock extends Block implements IIgniteProvider {
    private final CustomMaterial material;

    public CharmBookshelfBlock(CustomMaterial material) {
        super(Properties.ofFullCopy(Blocks.BOOKSHELF));
        this.material = material;
    }

    public CustomMaterial getMaterial() {
        return material;
    }

    @Override
    public int igniteChance() {
        return material.igniteChance();
    }

    @Override
    public int burnChance() {
        return material.burnChance();
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final CustomMaterial material;

        public BlockItem(Supplier<CharmBookshelfBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
