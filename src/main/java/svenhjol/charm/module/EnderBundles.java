package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.EnderBundlesClient;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.item.EnderBundleItem;

import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = EnderBundlesClient.class, description = "EnderBundles")
public class EnderBundles extends CharmModule {
    public static final Identifier MSG_SERVER_UPDATE_ENDER_INVENTORY = new Identifier(Charm.MOD_ID, "server_update_ender_inventory");
    public static final Identifier MSG_CLIENT_UPDATE_ENDER_INVENTORY = new Identifier(Charm.MOD_ID, "server_client_ender_inventory");
    public static final Identifier TRIGGER_USED_ENDER_BUNDLE = new Identifier(Charm.MOD_ID, "used_ender_bundle");

    public static EnderBundleItem ENDER_BUNDLE;

    @Override
    public void register() {
        ENDER_BUNDLE = new EnderBundleItem(this);

        // register server message handler to call the serverCallback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE_ENDER_INVENTORY, this::handleUpdateEnderInventory);
    }

    @Override
    public List<Identifier> advancements() {
        return Arrays.asList(new Identifier(Charm.MOD_ID, "use_ender_bundle"));
    }

    private void handleUpdateEnderInventory(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        server.execute(() -> {
            NbtCompound nbt = new NbtCompound();
            nbt.put("EnderItems", player.getEnderChestInventory().toNbtList());

            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            buffer.writeNbt(nbt);

            ServerPlayNetworking.send(player, MSG_CLIENT_UPDATE_ENDER_INVENTORY, buffer);
        });
    }

    public static void triggerUsedEnderBundle(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, EnderBundles.TRIGGER_USED_ENDER_BUNDLE);
    }
}