package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

@Mixin(AnvilScreen.class)
public class StopShowingTooExpensiveMixin {

    /**
     * A player in creative mode does not see the "Too Expensive" message.
     * Change the creative mode check to also check for the "remove too expensive"
     * config option in AnvilImprovements.
     *
     * This targets the *display* of the message on the client side.
     * @see RemoveTooExpensiveMixin for the server side.
     */
    @Redirect(
        method = "drawForeground",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"
        )
    )
    private boolean hookMaximumCostCheck(PlayerAbilities abilities, MatrixStack matrix, int x, int y) {
        return AnvilImprovements.removeTooExpensive() || abilities.creativeMode;
    }
}
