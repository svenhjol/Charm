package svenhjol.charm.mixin.allow_too_expensive;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.Charm;
import svenhjol.charm.module.allow_too_expensive.AllowTooExpensive;

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
        method = "renderLabels",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
        )
    )
    private boolean hookMaximumCostCheck(Abilities abilities, PoseStack matrix, int x, int y) {
        return Charm.LOADER.isEnabled(AllowTooExpensive.class) || abilities.instabuild;
    }
}
