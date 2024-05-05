package svenhjol.charm.feature.clear_item_frames;

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
import svenhjol.charm.api.event.EntityAttackEvent;
import svenhjol.charm.api.event.EntityUseEvent;
import svenhjol.charm.foundation.feature.Register;

import javax.annotation.Nullable;

public final class CommonRegistration extends Register<ClearItemFrames> {
    public CommonRegistration(ClearItemFrames feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        ClearItemFrames.particleType = feature.registry().particleType("apply_amethyst", ApplyAmethystParticleType::new);
    }

    @Override
    public void onEnabled() {
        EntityUseEvent.INSTANCE.handle(this::handleEntityUse);
        EntityAttackEvent.INSTANCE.handle(this::handleEntityAttack);
    }

    /**
     * Try and remove an amethyst shard from an itemframe.
     */
    private InteractionResult handleEntityAttack(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
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
                    CommonNetworking.RemoveAmethyst.send((ServerPlayer)player, frame.blockPosition());
                }

                frame.setInvisible(false);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Try and an amethyst shard to an itemframe.
     */
    private InteractionResult handleEntityUse(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
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
                CommonNetworking.AddAmethyst.send(serverPlayer, frame.blockPosition());
                ClearItemFrames.triggerMadeClearItemFrame(serverPlayer);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
