package svenhjol.charm.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.AnvilImprovements;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Redirect(
        method = "drawForeground",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"
        )
    )
    private boolean hookMaximumCostCheck(PlayerAbilities abilities, MatrixStack matrix, int x, int y) {
        return AnvilImprovements.allowTooExpensive() || abilities.creativeMode;
    }
}
