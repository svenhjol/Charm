package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.core.particles.SimpleParticleType;
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
import svenhjol.charm.Charm;
import svenhjol.charm.feature.clear_item_frames.ClearItemFramesNetwork.AddAmethyst;
import svenhjol.charm.feature.clear_item_frames.ClearItemFramesNetwork.RemoveAmethyst;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.EntityAttackEvent;
import svenhjol.charmony_api.event.EntityUseEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Add amethyst shards to item frames to make them invisible.")
public class ClearItemFrames extends CharmonyFeature {
    static Supplier<SimpleParticleType> particleType;

    @Override
    public void register() {
        particleType = Charm.instance().registry().particleType("apply_amethyst", ApplyAmethystParticleType::new);
        ClearItemFramesNetwork.register();
    }

    @Override
    public void runWhenEnabled() {
        EntityUseEvent.INSTANCE.handle(this::handleEntityUse);
        EntityAttackEvent.INSTANCE.handle(this::handleEntityAttack);
    }

    private InteractionResult handleEntityAttack(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrame frame) {
            var pos = frame.blockPosition();

            if (frame.isInvisible() && frame.getItem().isEmpty()) {
                var shard = new ItemStack(Items.AMETHYST_SHARD);
                var itemEntity = new ItemEntity(level, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, shard);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);

                if (!level.isClientSide()) {
                    RemoveAmethyst.send(frame.blockPosition(), (ServerPlayer)player);
                }

                frame.setInvisible(false);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleEntityUse(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        var held = player.getItemInHand(hand);
        if (held.getItem() != Items.AMETHYST_SHARD) {
            return InteractionResult.PASS;
        }

        // GlowItemFrameEntity extends ItemFrameEntity so this comparison is safe for both.
        if (entity instanceof ItemFrame frame) {
            // Already invisible, pass.
            if (frame.isInvisible()) {
                return InteractionResult.PASS;
            }

            frame.setInvisible(true);

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }

            if (!level.isClientSide()) {
                var serverPlayer = (ServerPlayer)player;
                AddAmethyst.send(frame.blockPosition(), serverPlayer);
                triggerMadeClearItemFrame(serverPlayer);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    public static void triggerMadeClearItemFrame(Player player) {
        Advancements.trigger(Charm.instance().makeId("made_clear_item_frame"), player);
    }
}
