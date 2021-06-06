package svenhjol.charm.mixin.atlases;

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
import svenhjol.charm.module.atlases.Atlases;

@Mixin(CartographyTableMenu.class)
public class ScaleAtlasesOnCartographyTableMixin {
    @Shadow @Final private ContainerLevelAccess context;

    @Shadow @Final private ResultContainer resultInventory;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void hookConstructor(int syncId, Inventory inventory, ContainerLevelAccess context, CallbackInfo ci) {
        Atlases.setupAtlasUpscale(inventory, (CartographyTableMenu) (Object) this);
    }

    @Inject(
        method = "updateResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookUpdateResult(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, CallbackInfo ci) {
        Level world = context.evaluate((w, b) -> w).orElse(null);
        if (world == null) return;
        if (Atlases.makeAtlasUpscaleOutput(topStack, bottomStack, outputStack, world, resultInventory, (CartographyTableMenu) (Object) this)) {
            ci.cancel();
        }
    }
}
