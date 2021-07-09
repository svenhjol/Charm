package svenhjol.charm.mixin.accessor;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
    @Invoker
    void invokeSetFlammable(Block block, int burnChance, int spreadChance);

    @Mutable @Accessor
    Object2IntMap<Block> getFlameOdds();

    @Mutable @Accessor
    Object2IntMap<Block> getBurnOdds();
}
