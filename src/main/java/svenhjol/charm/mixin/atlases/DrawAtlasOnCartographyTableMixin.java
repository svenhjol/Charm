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
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/CartographyTableScreen;renderResultingMap(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/Integer;Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;ZZZZ)V"
        ),
        index = 3
    )
    private boolean hookDrawBackground(boolean value) {
        if (AtlasesClient.shouldDrawAtlasCopy((CartographyTableScreen) (Object) this))
            return true;

        return value;
    }
}
