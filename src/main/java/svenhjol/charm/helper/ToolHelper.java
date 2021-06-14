package svenhjol.charm.helper;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.mixin.accessor.AxeItemAccessor;

import java.util.HashMap;
import java.util.Map;

public class ToolHelper {
    public static void addStrippableLog(Block raw, Block stripped) {
        makeStrippablesMutable();
        AxeItemAccessor.getStrippables().put(raw, stripped);
    }

    private static void makeStrippablesMutable() {
        Map<Block, Block> strippables = AxeItemAccessor.getStrippables();
        AxeItemAccessor.setStrippables(new HashMap<>(strippables));
    }
}
