package svenhjol.charm.mixin.unlimited_repair_cost;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    /**
     * A player in creative mode can repair or enchant without "too expensive".
     * Remove the message by always returning true for this check.
     * This modifies the updated output slot on the server side.
     * @see AnvilScreenMixin for the client side.
     */
    @Redirect(
        method = "createResult",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z",
            ordinal = 1
        )
    )
    private boolean hookCreateResultCheckAbilities(Abilities abilities) {
        return true;
    }
}
