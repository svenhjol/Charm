package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.CustomMaterial;

import java.util.function.Supplier;

public class CharmLeavesBlock extends LeavesBlock implements IIgniteProvider {
    protected final CustomMaterial material;

    public CharmLeavesBlock(CustomMaterial material) {
        super(Properties.of()
            .strength(0.2F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .noOcclusion()
            .isValidSpawn((state, world, pos, type) -> false)
            .isSuffocating((state, world, pos) -> false)
            .isViewBlocking((state, world, pos) -> false));

        this.material = material;
    }

    @Override
    public int igniteChance() {
        return material.isFlammable() ? 30 : 0;
    }

    @Override
    public int burnChance() {
        return material.isFlammable() ? 60 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
