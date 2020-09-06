package svenhjol.meson.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
    /**
     * Used by Meson abstract blocks to set flammability data.
     *
     * {@link svenhjol.meson.block.IMesonBlock#setFireInfo(int, int)}
     */
    @Invoker
    void invokeRegisterFlammableBlock(Block block, int encouragement, int flammability);
}
