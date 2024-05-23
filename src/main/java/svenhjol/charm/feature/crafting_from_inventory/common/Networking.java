package svenhjol.charm.feature.crafting_from_inventory.common;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Networking extends FeatureHolder<CraftingFromInventory> {
    public Networking(CraftingFromInventory feature) {
        super(feature);
    }

    // Client-to-server packet to instruct the server to try and open the crafting table.
    public record C2SOpenPortableCrafting() implements CustomPacketPayload {
        private static final String ID = "charm:open_portable_crafting";
        public static Type<C2SOpenPortableCrafting> TYPE = CustomPacketPayload.createType(ID);
        public static StreamCodec<FriendlyByteBuf, C2SOpenPortableCrafting> CODEC =
            StreamCodec.of(C2SOpenPortableCrafting::encode, C2SOpenPortableCrafting::decode);

        public static void send() {
            ClientPlayNetworking.send(new C2SOpenPortableCrafting());
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, C2SOpenPortableCrafting self) {}

        private static C2SOpenPortableCrafting decode(FriendlyByteBuf buf) {
            return new C2SOpenPortableCrafting();
        }
    }
}
