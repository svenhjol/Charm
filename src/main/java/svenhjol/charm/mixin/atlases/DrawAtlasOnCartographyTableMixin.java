package svenhjol.charm.mixin.atlases;

import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.module.atlases.AtlasesClient;

@Mixin(CartographyTableScreen.class)
public class DrawAtlasOnCartographyTableMixin {

    /**
     * Updates the cartography table renderer to show the atlas
     * when placed on the table.
     */
    @ModifyArg(
        method = "drawBackground",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/CartographyTableScreen;drawMap(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/Integer;Lnet/minecraft/item/map/MapState;ZZZZ)V"
        ),
        index = 3
    )
    private boolean hookDrawBackground(boolean value) {
        if (AtlasesClient.shouldDrawAtlasCopy((CartographyTableScreen) (Object) this))
            return true;

        return value;
    }
}
