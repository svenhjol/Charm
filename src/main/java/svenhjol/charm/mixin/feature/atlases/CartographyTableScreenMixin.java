package svenhjol.charm.mixin.feature.atlases;

import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.charmony.Resolve;

/**
 * Render the cartography table "scale" background when an atlas and an empty map are added to the slots.
 */
@SuppressWarnings("UnreachableCode")
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
        if (Resolve.feature(AtlasesClient.class).handlers.shouldDrawAtlasCopy((CartographyTableScreen) (Object) this)) {
            return true;
        }
        return value;
    }
}
