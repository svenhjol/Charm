package svenhjol.charm.base.helper;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import svenhjol.charm.mixin.accessor.AxeItemAccessor;
import svenhjol.charm.mixin.accessor.HoeItemAccessor;
import svenhjol.charm.mixin.accessor.PickaxeItemAccessor;
import svenhjol.charm.mixin.accessor.ShovelItemAccessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BlockHelper {
    public static void setEffectiveTool(Block block, Class<? extends MiningToolItem> clazz) {
        if (clazz == PickaxeItem.class) {
            List<Block> effectiveBlocks = new ArrayList<>(PickaxeItemAccessor.getEffectiveBlocks());
            effectiveBlocks.add(block);
            PickaxeItemAccessor.setEffectiveBlocks(new HashSet<>(effectiveBlocks));
        }

        if (clazz == AxeItem.class) {
            List<Block> effectiveBlocks = new ArrayList<>(AxeItemAccessor.getEffectiveBlocks());
            effectiveBlocks.add(block);
            AxeItemAccessor.setEffectiveBlocks(new HashSet<>(effectiveBlocks));
        }

        if (clazz == ShovelItem.class) {
            List<Block> effectiveBlocks = new ArrayList<>(ShovelItemAccessor.getEffectiveBlocks());
            effectiveBlocks.add(block);
            ShovelItemAccessor.setEffectiveBlocks(new HashSet<>(effectiveBlocks));
        }

        if (clazz == HoeItem.class) {
            List<Block> effectiveBlocks = new ArrayList<>(HoeItemAccessor.getEffectiveBlocks());
            effectiveBlocks.add(block);
            HoeItemAccessor.setEffectiveBlocks(new HashSet<>(effectiveBlocks));
        }
    }
}
