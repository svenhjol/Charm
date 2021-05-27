package svenhjol.charm.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(FireBlock.class)
@CharmMixin(required = true)
public interface FireBlockAccessor {
    /**
     * Used by abstract blocks to set flammability data.
     *
     * {@link ICharmBlock#setFireInfo(int, int)}
     */
    @Invoker
    void invokeRegisterFlammableBlock(Block block, int encouragement, int flammability);
}
