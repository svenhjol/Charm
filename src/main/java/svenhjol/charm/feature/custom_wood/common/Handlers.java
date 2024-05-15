package svenhjol.charm.feature.custom_wood.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.foundation.block.CharmChestBlock;
import svenhjol.charm.foundation.feature.FeatureHolder;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<CustomWood> {
    public Handlers(CustomWood feature) {
        super(feature);
    }

    @SuppressWarnings("deprecation")
    public void levelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            // Set each boat type's planks.
            CustomWoodHelper.getBoatPlanks().forEach(
                (type, id) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(
                    block -> type.planks = block));

            // Set each sign's block and item.
            CustomWoodHelper.getSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getSignBlock().get();
                sign.block.item = sign;
            });

            // Set each hanging sign's block and item.
            CustomWoodHelper.getHangingSignItems().forEach(supplier -> {
                var sign = supplier.get();
                sign.wallBlock = sign.getWallSignBlock().get();
                sign.wallBlock.item = sign;
                sign.block = sign.getHangingBlock().get();
                sign.block.item = sign;
            });
        }
    }

    public InteractionResult animalInteraction(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof AbstractChestedHorse horse) {
            var held = player.getItemInHand(hand);
            var item = held.getItem();
            var block = Block.byItem(item);

            if (block instanceof CharmChestBlock
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
