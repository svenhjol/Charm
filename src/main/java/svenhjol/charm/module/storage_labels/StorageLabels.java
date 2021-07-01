package svenhjol.charm.module.storage_labels;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmCommonModule;

import javax.annotation.Nullable;

@CommonModule(mod = Charm.MOD_ID, description = "Shows the custom name of a storage block as a floating label when the player is sneaking.")
public class StorageLabels extends CharmCommonModule {
    public static final ResourceLocation MSG_SERVER_QUERY_CUSTOM_NAME = new ResourceLocation(Charm.MOD_ID, "server_query_custom_name");
    public static final ResourceLocation MSG_CLIENT_UPDATE_CUSTOM_NAME = new ResourceLocation(Charm.MOD_ID, "client_update_custom_name");
    public static final ResourceLocation MSG_CLIENT_HAS_NO_CUSTOM_NAME = new ResourceLocation(Charm.MOD_ID, "client_has_no_custom_name");
    public static final ResourceLocation MSG_CLIENT_CLEAR_CUSTOM_NAME = new ResourceLocation(Charm.MOD_ID, "client_clear_custom_name");

    // hovering text scale factor
    public static final float SCALE = 0.010F;

    @Config(name = "Always show", description = "If true, floating labels will show even if the player is not sneaking.")
    public static boolean alwaysShow = false;

    @Config(name = "Show barrel labels", description = "If true, barrels with custom names will be displayed.")
    public static boolean showBarrelLabels = true;

    @Config(name = "Show chest labels", description = "If true, chests with custom names will be displayed.")
    public static boolean showChestLabels = true;

    @Config(name = "View distance", description = "Maximum squared distance at which label will be shown. Lower = player closer, Higher = player further.")
    public static int viewDistance = 10;

    @Override
    public void run() {
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_QUERY_CUSTOM_NAME, this::handleQueryCustomName);
        PlayerBlockBreakEvents.AFTER.register(this::handleBlockBreak);
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack held = player.getItemInHand(hand);

        if (held.getItem() == Items.NAME_TAG && held.hasCustomHoverName()) {
            BlockPos pos = hitResult.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof RandomizableContainerBlockEntity) {
                RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity)blockEntity;
                container.setCustomName(held.getHoverName());
                world.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.85F, 1.1F);

                if (!player.isCreative())
                    held.shrink(1);

                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    private void handleBlockBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (!world.isClientSide && blockEntity instanceof RandomizableContainerBlockEntity) {
            FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
            packet.writeLong(pos.asLong());
            ServerPlayNetworking.send((ServerPlayer) player, MSG_CLIENT_CLEAR_CUSTOM_NAME, packet);
        }
    }

    private void handleQueryCustomName(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());
        server.execute(() -> {
            ServerLevel world = (ServerLevel) player.level;
            if (world == null)
                return;

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RandomizableContainerBlockEntity) {
                RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity) blockEntity;

                FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
                packet.writeLong(pos.asLong());

                if (container.hasCustomName()) {
                    packet.writeUtf(container.getDisplayName().getContents());
                    ServerPlayNetworking.send(player, MSG_CLIENT_UPDATE_CUSTOM_NAME, packet);
                } else {
                    ServerPlayNetworking.send(player, MSG_CLIENT_HAS_NO_CUSTOM_NAME, packet);
                }
            }
        });
    }
}
