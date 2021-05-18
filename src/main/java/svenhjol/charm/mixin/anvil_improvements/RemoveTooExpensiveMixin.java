package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.AnvilImprovements;

@Mixin(AnvilScreenHandler.class)
public class RemoveTooExpensiveMixin {

    /**
     * A player in creative mode can repair or enchant without "too expensive".
     * Change the creative mode check to also check for the "remove too expensive"
     * config option in AnvilImprovements.
     *
     * This modifies the updated output slot on the server side.
     * @see StopShowingTooExpensiveMixin for the client side.
     */
    @Redirect(
        method = "updateResult",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z",
            ordinal = 1
        )
    )
    private boolean hookUpdateResultTooExpensive(PlayerAbilities abilities) {
        return AnvilImprovements.removeTooExpensive() || abilities.creativeMode;
    }
}
