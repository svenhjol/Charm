package svenhjol.charm.module.clear_item_frames;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.clear_item_frames.ClearItemFramesClient;

@Module(mod = Charm.MOD_ID, client = ClearItemFramesClient.class, description = "Add amethyst shards to item frames to make them invisible.")
public class ClearItemFrames extends CharmModule {
    public static final ResourceLocation MSG_CLIENT_ADD_AMETHYST = new ResourceLocation(Charm.MOD_ID, "client_add_amethyst");
    public static final ResourceLocation MSG_CLIENT_REMOVE_AMETHYST = new ResourceLocation(Charm.MOD_ID, "client_remove_amethyst");
    public static final ResourceLocation TRIGGER_USED_AMETHYST_ON_FRAME = new ResourceLocation(Charm.MOD_ID, "used_amethyst_on_frame");

    @Override
    public void init() {
        UseEntityCallback.EVENT.register(this::handleUseEntity);
        AttackEntityCallback.EVENT.register(this::handleAttackEntity);
    }

    private InteractionResult handleUseEntity(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() != Items.AMETHYST_SHARD)
            return InteractionResult.PASS;

        // GlowItemFrameEntity extends ItemFrameEntity so this comparison is safe for both
        if (entity instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) entity;

            // already invisible, pass
            if (frame.isInvisible())
                return InteractionResult.PASS;

            frame.setInvisible(true);
            held.shrink(1);

            if (!world.isClientSide) {
                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                data.writeLong(entity.blockPosition().asLong());
                ServerPlayNetworking.send((ServerPlayer) player, MSG_CLIENT_ADD_AMETHYST, data);
                CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayer) player, TRIGGER_USED_AMETHYST_ON_FRAME);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    public InteractionResult handleAttackEntity(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) entity;
            BlockPos pos = entity.blockPosition();

            if (frame.isInvisible()) {
                ItemStack shard = new ItemStack(Items.AMETHYST_SHARD);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, shard);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);

                if (!world.isClientSide) {
                    FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                    data.writeLong(entity.blockPosition().asLong());
                    ServerPlayNetworking.send((ServerPlayer) player, MSG_CLIENT_REMOVE_AMETHYST, data);
                }

                frame.setInvisible(false);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}
