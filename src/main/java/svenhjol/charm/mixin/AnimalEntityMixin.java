package svenhjol.charm.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.AutoRestock;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {
    @Inject(method = "eat", at = @At("HEAD"))
    public void hookConsumeItemFromStack(PlayerEntity player, ItemStack stack, CallbackInfo callbackInfo) {
        AutoRestock.addItemUsedStat(player, stack);
    }
}
