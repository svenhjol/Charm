package svenhjol.charm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.PlayerDropInventoryCallback;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.module.AerialAffinityEnchantment;
import svenhjol.charm.module.ParrotsStayOnShoulder;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    @Shadow private long shoulderEntityAddedTime;

    @Shadow @Final public PlayerInventory inventory;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "dropShoulderEntities",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSpawnShoulderEntities(CallbackInfo ci) {
        if (ParrotsStayOnShoulder.shouldParrotStayMounted(this.world, this.shoulderEntityAddedTime))
            ci.cancel();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((PlayerEntity)(Object)this);
    }

    @Inject(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"
        ),
        cancellable = true
    )
    private void hookDropInventory(CallbackInfo ci) {
        ActionResult result = PlayerDropInventoryCallback.EVENT.invoker().interact((PlayerEntity) (Object) this, this.inventory);
        if (result == ActionResult.SUCCESS)
            ci.cancel();
    }

    // This is broken in Loom 0.8-SNAPSHOT because onGround doesn't obfuscate when in prod.
    // Use the inject/cancel version until it is fixed.
//    @Redirect(
//        method = "getBlockBreakingSpeed",
//        at = @At(
//            value = "FIELD",
//            target = "Lnet/minecraft/entity/player/PlayerEntity;onGround:Z"
//        )
//    )
//    private boolean hookDigSpeedOnGround(PlayerEntity player, BlockState state) {
//        return player.isOnGround() || AerialAffinityEnchantment.digFast(player);
//    }
    @Inject(
        method = "getBlockBreakingSpeed",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookDigSpeedOnGround(BlockState block, CallbackInfoReturnable<Float> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (!player.isOnGround() && AerialAffinityEnchantment.digFast(player)) {
            Float f = cir.getReturnValue();
            cir.setReturnValue(f * 5.0F);
        }
    }
}
