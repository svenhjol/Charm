package svenhjol.charm.mixin.accessor;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Mutable @Accessor("STRIPPABLES")
    static Map<Block, Block> getStrippables() {
        throw new IllegalStateException();
    }

    @Mutable @Accessor("STRIPPABLES")
    static void setStrippables(Map<Block, Block> map) {
        throw new IllegalStateException();
    }
}
