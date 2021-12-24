package svenhjol.charm.module.clear_item_frames;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.clear_item_frames.network.ServerSendAddAmethyst;
import svenhjol.charm.module.clear_item_frames.network.ServerSendRemoveAmethyst;

@CommonModule(mod = Charm.MOD_ID, description = "Add amethyst shards to item frames to make them invisible.")
public class ClearItemFrames extends CharmModule {
    public static final ResourceLocation TRIGGER_USED_AMETHYST_ON_FRAME = new ResourceLocation(Charm.MOD_ID, "used_amethyst_on_frame");

    public static ServerSendAddAmethyst SERVER_SEND_ADD_AMETHYST;
    public static ServerSendRemoveAmethyst SERVER_SEND_REMOVE_AMETHYST;

    @Override
    public void runWhenEnabled() {
        UseEntityCallback.EVENT.register(this::handleUseEntity);
        AttackEntityCallback.EVENT.register(this::handleAttackEntity);

        SERVER_SEND_ADD_AMETHYST = new ServerSendAddAmethyst();
        SERVER_SEND_REMOVE_AMETHYST = new ServerSendRemoveAmethyst();
    }

    private InteractionResult handleUseEntity(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() != Items.AMETHYST_SHARD) {
            return InteractionResult.PASS;
        }

        // GlowItemFrameEntity extends ItemFrameEntity so this comparison is safe for both
        if (entity instanceof ItemFrame frame) {

            // already invisible, pass
            if (frame.isInvisible()) {
                return InteractionResult.PASS;
            }

            frame.setInvisible(true);
            held.shrink(1);

            if (!level.isClientSide) {
                SERVER_SEND_ADD_AMETHYST.send((ServerPlayer) player, frame.blockPosition());
                CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer) player, TRIGGER_USED_AMETHYST_ON_FRAME);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public InteractionResult handleAttackEntity(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrame frame) {
            BlockPos pos = frame.blockPosition();

            if (frame.isInvisible()) {
                ItemStack shard = new ItemStack(Items.AMETHYST_SHARD);
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, shard);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);

                if (!level.isClientSide) {
                    SERVER_SEND_REMOVE_AMETHYST.send((ServerPlayer) player, frame.blockPosition());
                }

                frame.setInvisible(false);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}
