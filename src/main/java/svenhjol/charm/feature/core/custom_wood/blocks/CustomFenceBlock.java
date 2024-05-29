package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.IgniteProvider;

import java.util.function.Supplier;

public class CustomFenceBlock extends FenceBlock implements IgniteProvider {
    protected final CustomMaterial material;

    public CustomFenceBlock(CustomMaterial material) {
        super(material.blockProperties()
            .strength(2.0F, 3.0F));

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
