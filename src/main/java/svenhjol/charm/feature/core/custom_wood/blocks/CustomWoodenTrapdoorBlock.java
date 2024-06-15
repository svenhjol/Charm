package svenhjol.charm.feature.core.custom_wood.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import svenhjol.charm.charmony.iface.CustomMaterial;
import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.charmony.iface.IgniteProvider;

import java.util.function.Supplier;

public class CustomWoodenTrapdoorBlock extends TrapDoorBlock implements IgniteProvider {
    protected final CustomMaterial material;

    public CustomWoodenTrapdoorBlock(CustomWoodMaterial material) {
        super(material.blockProperties()
            .noOcclusion()
            .strength(3.0F)
            .isValidSpawn((state, world, pos, type) -> false), material.blockSetType());

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
