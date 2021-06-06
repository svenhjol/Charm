package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(FireBlock.class)
@CharmMixin(required = true)
public interface FireBlockAccessor {
    @Invoker
    void invokeSetFlammable(Block block, int burnChance, int spreadChance);
}
