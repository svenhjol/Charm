package svenhjol.charm.feature.item_frame_hiding.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;
import svenhjol.charm.charmony.feature.FeatureHolder;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<ItemFrameHiding> {
    public Handlers(ItemFrameHiding feature) {
        super(feature);
    }

    /**
     * Remove amethyst shard from an itemframe.
     */
    public InteractionResult entityAttack(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrame frame) {
            var pos = frame.blockPosition();

            if (frame.isInvisible()) {
                if (frame.getItem().isEmpty()) {
                    return InteractionResult.PASS;
                }

                var shard = new ItemStack(Items.AMETHYST_SHARD);
                var itemEntity = new ItemEntity(level, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, shard);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);

                if (!level.isClientSide()) {
                    var framePos = frame.blockPosition();
                    PlayerHelper.getPlayersInRange(level, framePos, 8.0d).forEach(p -> 
                        Networking.S2CRemoveAmethyst.send((ServerPlayer)p, framePos));
                }

                frame.setInvisible(false);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Add amethyst shard to an itemframe.
     */
    public InteractionResult entityUse(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        var held = player.getItemInHand(hand);
        if (held.getItem() != Items.AMETHYST_SHARD) {
            return InteractionResult.PASS;
        }

        // GlowItemFrameEntity extends ItemFrameEntity so this comparison is safe for both.
        if (entity instanceof ItemFrame frame) {
            // If there's no item yet, pass.
            if (frame.getItem().isEmpty()) {
                return InteractionResult.PASS;
            }

            // If already invisible, pass.
            if (frame.isInvisible()) {
                return InteractionResult.PASS;
            }

            frame.setInvisible(true);

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }

            if (!level.isClientSide()) {
                var serverPlayer = (ServerPlayer)player;
                var framePos = frame.blockPosition();
                PlayerHelper.getPlayersInRange(level, framePos, 8.0d).forEach(p -> {
                    Networking.S2CAddAmethyst.send((ServerPlayer)p, framePos);
                    feature().advancements.hiddenItemFrame(serverPlayer);
                });
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
