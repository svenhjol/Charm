package svenhjol.charm.mixin.armor_invisibility;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.armor_invisibility.ArmorInvisibility;

@Mixin(LivingEntity.class)
public abstract class ReduceAttackDistanceMixin {

    /**
     * Defer checking the attack distance of a player to the shouldArmorBeInvisible method.
     * When this method returns true, the distance will not be increased.
     *
     * This means that mobs will not detect an invisible player wearing armor that
     * turns invisible e.g. leather and chainmail.
     */
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(
        method = "getArmorCoverPercentage",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void hookGetArmorVisibility(CallbackInfoReturnable<Float> cir) {
        if (ModuleHandler.enabled("charm:armor_invisibility")) {
            LivingEntity entity = (LivingEntity) (Object) this;
            Iterable<ItemStack> armorItems = this.getArmorSlots();

            int i = 0;
            int j = 0;

            for (ItemStack itemstack : armorItems) {
                if (!ArmorInvisibility.shouldArmorBeInvisible(entity, itemstack)) {
                    ++j;
                }
                ++i;
            }

            cir.setReturnValue(i > 0 ? (float)j / (float)i : 0.0F);
        }
    }
}
