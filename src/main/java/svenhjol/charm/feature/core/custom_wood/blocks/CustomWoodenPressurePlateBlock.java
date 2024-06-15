package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;

import java.util.function.Supplier;

public class CustomWoodenPressurePlateBlock extends PressurePlateBlock {
    protected final CustomMaterial material;

    public CustomWoodenPressurePlateBlock(CustomWoodMaterial material) {
        super(Sensitivity.EVERYTHING, material.blockProperties()
            .strength(0.5F)
            .noCollission(), material.blockSetType());

        this.material = material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
