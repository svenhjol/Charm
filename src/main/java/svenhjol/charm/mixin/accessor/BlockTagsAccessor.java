package svenhjol.charm.mixin.accessor;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTags.class)
public interface BlockTagsAccessor {
    @Invoker()
    static Tag.Named<Block> invokeBind(String id) {
        throw new IllegalStateException();
    }
}
