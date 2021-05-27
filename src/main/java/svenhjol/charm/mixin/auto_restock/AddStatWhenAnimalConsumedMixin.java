package svenhjol.charm.mixin.auto_restock;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.auto_restock.AutoRestock;

@Mixin(AnimalEntity.class)
public class AddStatWhenAnimalConsumedMixin {

    /**
     * Allows autorestock of an item fed to an animal.
     */
    @Inject(method = "eat", at = @At("HEAD"))
    public void hookConsumeItemFromStack(PlayerEntity player, Hand hand, ItemStack stack, CallbackInfo callbackInfo) {
        AutoRestock.addItemUsedStat(player, stack);
    }
}
