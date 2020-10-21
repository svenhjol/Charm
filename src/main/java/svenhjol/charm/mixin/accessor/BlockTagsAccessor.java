package svenhjol.charm.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTags.class)
public interface BlockTagsAccessor {
    @Invoker()
    static Tag.Identified<Block> invokeRegister(String id) {
        throw new IllegalStateException();
    }
}
