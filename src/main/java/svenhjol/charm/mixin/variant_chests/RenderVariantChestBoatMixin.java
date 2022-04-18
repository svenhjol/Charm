package svenhjol.charm.mixin.variant_chests;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.Charm;
import svenhjol.charm.module.variant_chests.IVariantChestBoat;

/**
 * Render a custom texture when the ChestBoat entity has a variant chest.
 */
@Mixin(BoatRenderer.class)
public class RenderVariantChestBoatMixin {
    private final ThreadLocal<Boat> boat = new ThreadLocal<>();

    @Inject(
        method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD")
    )
    private void hookGetBoat(Boat boat, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        this.boat.remove();
        this.boat.set(boat);
    }

    @ModifyArg(
        method = "render(Lnet/minecraft/world/entity/vehicle/Boat;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/BoatModel;renderType(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
        ),
        index = 0
    )
    private ResourceLocation hookChangeTexture(ResourceLocation res) {
        var boat = this.boat.get();
        if (boat instanceof IVariantChestBoat chestBoat) {
            var chestType = chestBoat.getVariantChest();

            if (!chestType.isEmpty()) {
                var boatType = boat.getBoatType();
                return new ResourceLocation(Charm.MOD_ID, "textures/entity/chest_boat/" + boatType + "_with_" + chestType + "_chest.png");
            }
        }

        return res;
    }
}
