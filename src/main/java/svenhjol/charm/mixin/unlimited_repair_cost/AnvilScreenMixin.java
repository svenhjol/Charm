package svenhjol.charm.mixin.unlimited_repair_cost;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    /**
     * A player in creative mode does not see the "Too Expensive" message.
     * Remove the message by always returning true for this check.
     * This targets the *display* of the message on the client side.
     * @see AnvilMenuMixin for the server side.
     */
    @Redirect(
        method = "renderLabels",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
        )
    )
    private boolean hookRenderLabelsCheckAbilities(Abilities abilities, GuiGraphics guiGraphics, int x, int y) {
        return true;
    }
}
