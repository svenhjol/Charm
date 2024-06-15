package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import svenhjol.charm.charmony.iface.CustomMaterial;

import java.util.function.Supplier;

public class CustomSaplingBlock extends SaplingBlock {
    protected final CustomMaterial material;

    public CustomSaplingBlock(CustomMaterial material, AbstractTreeGrower generator) {
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
