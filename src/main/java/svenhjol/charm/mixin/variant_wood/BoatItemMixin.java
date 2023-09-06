package svenhjol.charm.mixin.variant_wood;

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
import svenhjol.charm.feature.variant_wood.iface.IVariantChestBoat;
import svenhjol.charm.feature.variant_wood.recipe.VariantChestBoatRecipe;

import java.util.List;

/**
 * Adds the variant chest type from a chest boat item to a ChestBoat entity.
 */
@Mixin(BoatItem.class)
public class BoatItemMixin {
    @Inject(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookUse(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                         ItemStack held, HitResult hitResult, Vec3 vec3, double d, List<?> list, Boat boat) {
        if (boat instanceof IVariantChestBoat chestBoat) {
            var tag = held.getTag();

            if (tag != null && tag.contains(VariantChestBoatRecipe.CHEST_TYPE_TAG)) {
                var type = tag.getString(VariantChestBoatRecipe.CHEST_TYPE_TAG);
                chestBoat.setVariantChest(type);
            }
        }
    }
}
