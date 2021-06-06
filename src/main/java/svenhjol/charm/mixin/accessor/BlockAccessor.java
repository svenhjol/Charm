package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Block.class)
@CharmMixin(required = true)
public interface BlockAccessor {
    /**
     * Used by OverrideHandler to change translation key of a registered Block.
     *
     * {@link svenhjol.charm.helper.OverrideHandler#changeBlockTranslationKey(Block, String)}
     */
    @Accessor()
    void setTranslationKey(String key);
}
