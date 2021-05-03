package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.Atlases;

@Mixin(CartographyTableScreenHandler.class)
public class CartographyTableScreenHandlerMixin {
    @Shadow @Final private ScreenHandlerContext context;

    @Shadow @Final private CraftingResultInventory resultInventory;

    @Inject(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V", at = @At("TAIL"))
    private void hookConstructor(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo ci) {
        Atlases.setupAtlasUpscale(inventory, (CartographyTableScreenHandler) (Object) this);
    }

    @Inject(
        method = "updateResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookUpdateResult(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, CallbackInfo ci) {
        World world = context.get((w, b) -> w).orElse(null);
        if (world == null) return;
        if (Atlases.makeAtlasUpscaleOutput(topStack, bottomStack, outputStack, world, resultInventory, (CartographyTableScreenHandler) (Object) this)) {
            ci.cancel();
        }
    }
}
