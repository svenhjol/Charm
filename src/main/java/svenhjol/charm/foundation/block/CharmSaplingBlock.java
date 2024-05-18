package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmSaplingBlock extends SaplingBlock {
    protected final CustomMaterial material;

    public CharmSaplingBlock(CustomMaterial material, TreeGrower generator) {
        super(generator, Properties.of()
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS));

        this.material = material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
