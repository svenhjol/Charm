package svenhjol.charm.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

import java.util.Set;

@Mixin(BlockEntityType.class)
@CharmMixin(required = true)
public interface BlockEntityTypeAccessor {
    @Accessor("blocks")
    Set<Block> getBlocks();

    @Accessor("blocks")
    void setBlocks(Set<Block> blocks);
}
