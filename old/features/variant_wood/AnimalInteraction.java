package svenhjol.charm.feature.variant_wood;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class AnimalInteraction {
    public static InteractionResult handle(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof AbstractChestedHorse horse) {
            var held = player.getItemInHand(hand);
            var item = held.getItem();
            var block = Block.byItem(item);

            if (block instanceof VariantChestBlock
                && horse.isTamed()
                && !horse.hasChest()
                && !horse.isBaby()
            ) {
                var random = horse.getRandom();
                horse.setChest(true);
                horse.playSound(SoundEvents.DONKEY_CHEST, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);

                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                }

                horse.createInventory();
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}
