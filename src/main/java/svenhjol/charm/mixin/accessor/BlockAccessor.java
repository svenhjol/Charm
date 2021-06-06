package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Block.class)
@CharmMixin(required = true)
public interface BlockAccessor {
    @Accessor()
    void setDescriptionId(String key);
}
