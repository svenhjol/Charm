package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.api.iface.IVariantWoodMaterial;

import java.util.function.Supplier;

public class CharmWoodenTrapdoorBlock extends TrapDoorBlock implements IIgniteProvider {
    protected final IVariantMaterial variantMaterial;

    public CharmWoodenTrapdoorBlock(IVariantWoodMaterial material) {
        super(material.blockSetType(), material.blockProperties()
            .noOcclusion()
            .strength(3.0F)
            .isValidSpawn((state, world, pos, type) -> false));

        this.variantMaterial = material;
    }

    @Override
    public int igniteChance() {
        return variantMaterial.isFlammable() ? 5 : 0;
    }

    @Override
    public int burnChance() {
        return variantMaterial.isFlammable() ? 20 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
