package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.iface.IgniteProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmPlanksBlock extends Block implements IgniteProvider {
    protected final CustomMaterial material;

    public CharmPlanksBlock(CustomMaterial material) {
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
        public BlockItem(Supplier<CharmPlanksBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
