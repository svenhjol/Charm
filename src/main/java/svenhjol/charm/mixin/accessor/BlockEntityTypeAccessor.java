package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Mixin(BlockEntityType.class)
@CharmMixin(required = true)
public interface BlockEntityTypeAccessor {
    @Accessor("blocks")
    Set<Block> getBlocks();

    @Accessor("blocks")
    void setBlocks(Set<Block> blocks);
}
