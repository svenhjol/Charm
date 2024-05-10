package svenhjol.charm.mixin.feature.atlases;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.FeatureResolver;

/**
 * Scale atlases on cartography table.
 */
@SuppressWarnings("UnreachableCode")
@Mixin(CartographyTableMenu.class)
public class CartographyTableMenuMixin implements FeatureResolver<Atlases> {
    @Shadow @Final private ContainerLevelAccess access;

    @Shadow @Final private ResultContainer resultContainer;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("TAIL")
    )
    private void hookConstructor(int syncId, Inventory inventory, ContainerLevelAccess context, CallbackInfo ci) {
        feature().handlers.setupAtlasUpscale(inventory, (CartographyTableMenu) (Object) this);
    }

    @Inject(
        method = "setupResultSlot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookUpdateResult(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, CallbackInfo ci) {
        Level level = access.evaluate((w, b) -> w).orElse(null);
        if (level == null) return;
        if (feature().handlers.makeAtlasUpscaleOutput(topStack, bottomStack, outputStack, level, resultContainer,
            (CartographyTableMenu) (Object) this)) {
            ci.cancel();
        }
    }

    @Override
    public Class<Atlases> featureType() {
        return Atlases.class;
    }
}
