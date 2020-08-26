package svenhjol.charm.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Blocks.class)
public interface BlocksAccessor {
    @Accessor("LANTERN")
    static void setLantern(Block lantern) { throw new IllegalStateException(); }

    @Accessor("SOUL_LANTERN")
    static void setSoulLantern(Block soulLantern) { throw new IllegalStateException(); }
}
