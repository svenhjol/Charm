package svenhjol.charm.mixin.feature.item_stacking;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean hasInfiniteMaterials();

    @Shadow public abstract Inventory getInventory();

    @Shadow public abstract @Nullable ItemEntity drop(ItemStack itemStack, boolean bl);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(
        method = "eat",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;isClientSide()Z"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookEat(Level level, ItemStack itemStack, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir,
                         ItemStack itemStack2, Optional<ItemStack> optional) {
        if (!level.isClientSide() && optional.isPresent()) {
            var convertsTo = optional.get().copy();
            var result = this.getInventory().getSlotWithRemainingSpace(convertsTo);
            if (result == -1) {
                this.drop(convertsTo, false);
            }
        }
    }
}
