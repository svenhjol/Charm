package svenhjol.charm.base.helper;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import svenhjol.charm.mixin.accessor.MiningToolItemAccessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolHelper {
    /**
     * Hoes have hardcoded effective blocks instead of a tag.
     * Use the Fabric HOES tag to iterate through each one, then use the accessor
     * to get the list of effective blocks, set the list mutability, add the required
     * blocks, then write back the list.
     */
    public static void addHoeEffectiveBlocks(Block... blocks) {
        for (Item hoe : FabricToolTags.HOES.values()) {
            Set<Block> effectiveBlocks = ((MiningToolItemAccessor) hoe).getEffectiveBlocks();
            List<Block> mutable = new ArrayList<>(effectiveBlocks);

            for (Block block : blocks) {
                if (!mutable.contains(block))
                    mutable.add(block);
            }

            ((MiningToolItemAccessor) hoe).setEffectiveBlocks(new HashSet<>(mutable));
        }
    }
}
