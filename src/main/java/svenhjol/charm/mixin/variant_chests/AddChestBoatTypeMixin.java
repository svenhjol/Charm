package svenhjol.charm.mixin.variant_chests;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.variant_chests.IVariantChestBoat;
import svenhjol.charm.module.variant_chests.VariantChestBoatRecipe;

import java.util.List;

@Mixin(BoatItem.class)
public class AddChestBoatTypeMixin {
    @Inject(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                         ItemStack held, HitResult hitResult, Vec3 vec3, double d, List list, Boat boat) {
        if (boat instanceof IVariantChestBoat chestBoat) {
            var tag = held.getTag();
            if (tag != null && tag.contains(VariantChestBoatRecipe.CHEST_TYPE_TAG)) {
                var type = tag.getString(VariantChestBoatRecipe.CHEST_TYPE_TAG);
                chestBoat.setVariantChest(type);
            }
        }
    }
}
