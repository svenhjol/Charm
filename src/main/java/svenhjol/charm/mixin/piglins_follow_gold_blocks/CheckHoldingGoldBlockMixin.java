package svenhjol.charm.mixin.piglins_follow_gold_blocks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class CheckHoldingGoldBlockMixin {
    @Inject(
        method = "isWearingGold",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookIsWearingGold(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (!(livingEntity instanceof Player)) return;
        Player player = (Player)livingEntity;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        Item targetItem = Items.GOLD_BLOCK;
        if (mainHand.getItem() == targetItem || offHand.getItem() == targetItem) {
            cir.setReturnValue(true);
        }
    }

}
