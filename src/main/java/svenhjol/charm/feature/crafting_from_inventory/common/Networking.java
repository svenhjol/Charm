package svenhjol.charm.feature.crafting_from_inventory.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Networking extends FeatureHolder<CraftingFromInventory> {
    public Networking(CraftingFromInventory feature) {
        super(feature);
    }

    @Packet(
            id = "charm:open_portable_crafting",
            description = "Client-to-server packet to instruct the server to try and open the crafting table."
    )
    public static class C2SOpenPortableCrafting implements PacketRequest {
        public C2SOpenPortableCrafting(){}

        public static void send() {
            var message = new C2SOpenPortableCrafting();
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ClientPlayNetworking.send(message.id(), buffer);
        }
    }
}
