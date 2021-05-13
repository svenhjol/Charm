package svenhjol.charm.mixin.accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("field_2776") // TODO: this will almost certainly be renamed to x
    int getX();

    @Accessor("field_2800") // TODO: this will alsmost certainly be renamed to y
    int getY();
}