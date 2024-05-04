package svenhjol.charm.mixin.feature.auto_restock;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.auto_restock.CommonCallbacks;

@Mixin(Animal.class)
public class AnimalMixin {
    /**
     * Allows auto restock of an item fed to an animal.
     */
    @Inject(
        method = "usePlayerItem",
        at = @At("HEAD")
    )
    public void hookConsumeItemFromStack(Player player, InteractionHand hand, ItemStack stack, CallbackInfo callbackInfo) {
        CommonCallbacks.addItemUsedStat(player, stack);
    }
}
