package svenhjol.charm.mixin.allow_too_expensive;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.allow_too_expensive.AllowTooExpensive;

@Mixin(AnvilMenu.class)
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
        method = "createResult",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z",
            ordinal = 1
        )
    )
    private boolean hookUpdateResultTooExpensive(Abilities abilities) {
        return AllowTooExpensive.enabled() || abilities.instabuild;
    }
}
