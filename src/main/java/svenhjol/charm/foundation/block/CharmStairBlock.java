package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmStairBlock extends StairBlock implements IIgniteProvider {
    protected final CustomMaterial material;

    public CharmStairBlock(CustomMaterial material, BlockState state) {
        super(state, material.blockProperties());
        this.material = material;
    }

    @Override
    public int igniteChance() {
        return material.isFlammable() ? 5 : 0;
    }

    @Override
    public int burnChance() {
        return material.isFlammable() ? 20 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
