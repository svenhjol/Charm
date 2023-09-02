package svenhjol.charm.mixin.atlases;

import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.feature.atlases.AtlasesClient;

/**
 * Render the cartography table "scale" background when an atlas and an empty map are added to the slots.
 */
@Mixin(CartographyTableScreen.class)
public class CartographyTableScreenMixin {
    @ModifyArg(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CartographyTableScreen;renderResultingMap(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/Integer;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;ZZZZ)V"
        ),
        index = 4 // the boolean flag to render the "scale" background
    )
    private boolean hookDrawBackground(boolean value) {
        if (AtlasesClient.shouldDrawAtlasCopy((CartographyTableScreen) (Object) this)) {
            return true;
        }

        return value;
    }
}
