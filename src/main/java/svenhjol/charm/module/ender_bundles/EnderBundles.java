package svenhjol.charm.module.ender_bundles;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.module.CharmModule;

@Module(mod = Charm.MOD_ID, client = EnderBundlesClient.class, description = "Ender bundles allow transfer of items to and from your ender chest.",
    requiresMixins = {"RenderTooltipCallback"})
public class EnderBundles extends CharmModule {
    public static final ResourceLocation MSG_SERVER_UPDATE_ENDER_INVENTORY = new ResourceLocation(Charm.MOD_ID, "server_update_ender_inventory");
    public static final ResourceLocation MSG_CLIENT_UPDATE_ENDER_INVENTORY = new ResourceLocation(Charm.MOD_ID, "server_client_ender_inventory");
    public static final ResourceLocation TRIGGER_USED_ENDER_BUNDLE = new ResourceLocation(Charm.MOD_ID, "used_ender_bundle");

    public static EnderBundleItem ENDER_BUNDLE;

    @Override
    public void register() {
        ENDER_BUNDLE = new EnderBundleItem(this);

        // register server message handler to call the serverCallback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE_ENDER_INVENTORY, this::handleUpdateEnderInventory);
    }

    private void handleUpdateEnderInventory(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        server.execute(() -> {
            CompoundTag nbt = new CompoundTag();
            nbt.put("EnderItems", player.getEnderChestInventory().createTag());

            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeNbt(nbt);

            ServerPlayNetworking.send(player, MSG_CLIENT_UPDATE_ENDER_INVENTORY, buffer);
        });
    }

    public static void triggerUsedEnderBundle(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, EnderBundles.TRIGGER_USED_ENDER_BUNDLE);
    }
}