package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Set;

@Mixin(BlockEntityType.class)
@CharmMixin(required = true)
public interface BlockEntityTypeAccessor {
    @Accessor("validBlocks")
    Set<Block> getValidBlocks();

    @Accessor("validBlocks")
    void setValidBlocks(Set<Block> blocks);
}
