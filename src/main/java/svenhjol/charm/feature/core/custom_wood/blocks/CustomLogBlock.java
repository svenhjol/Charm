package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import svenhjol.charm.api.iface.IgniteProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CustomLogBlock extends RotatedPillarBlock implements IgniteProvider {
    protected final CustomMaterial material;

    public CustomLogBlock(CustomMaterial material) {
        super(material.blockProperties()
            .strength(2.0F));

        this.material = material;
    }

    @Override
    public int igniteChance() {
        return material.isFlammable() ? 5 : 0;
    }

    @Override
    public int burnChance() {
        return material.isFlammable() ? 5 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
