package svenhjol.charm.module.clear_item_frames;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, client = ClearItemFramesClient.class, description = "Add amethyst shards to item frames to make them invisible.")
public class ClearItemFrames extends CharmModule {
    public static final Identifier MSG_CLIENT_ADD_AMETHYST = new Identifier(Charm.MOD_ID, "client_add_amethyst");
    public static final Identifier MSG_CLIENT_REMOVE_AMETHYST = new Identifier(Charm.MOD_ID, "client_remove_amethyst");
    public static final Identifier TRIGGER_USED_AMETHYST_ON_FRAME = new Identifier(Charm.MOD_ID, "used_amethyst_on_frame");

    @Override
    public void init() {
        UseEntityCallback.EVENT.register(this::handleUseEntity);
        AttackEntityCallback.EVENT.register(this::handleAttackEntity);
    }

    private ActionResult handleUseEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        ItemStack held = player.getStackInHand(hand);
        if (held.getItem() != Items.AMETHYST_SHARD)
            return ActionResult.PASS;

        // GlowItemFrameEntity extends ItemFrameEntity so this comparison is safe for both
        if (entity instanceof ItemFrameEntity) {
            ItemFrameEntity frame = (ItemFrameEntity) entity;

            // already invisible, pass
            if (frame.isInvisible())
                return ActionResult.PASS;

            frame.setInvisible(true);
            held.decrement(1);

            if (!world.isClient) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeLong(entity.getBlockPos().asLong());
                ServerPlayNetworking.send((ServerPlayerEntity) player, MSG_CLIENT_ADD_AMETHYST, data);
                CharmAdvancements.ACTION_PERFORMED.trigger((ServerPlayerEntity) player, TRIGGER_USED_AMETHYST_ON_FRAME);
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    public ActionResult handleAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrameEntity) {
            ItemFrameEntity frame = (ItemFrameEntity) entity;
            BlockPos pos = entity.getBlockPos();

            if (frame.isInvisible()) {
                ItemStack shard = new ItemStack(Items.AMETHYST_SHARD);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, shard);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);

                if (!world.isClient) {
                    PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                    data.writeLong(entity.getBlockPos().asLong());
                    ServerPlayNetworking.send((ServerPlayerEntity) player, MSG_CLIENT_REMOVE_AMETHYST, data);
                }

                frame.setInvisible(false);
                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.PASS;
    }
}
