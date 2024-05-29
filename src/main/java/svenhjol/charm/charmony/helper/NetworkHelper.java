package svenhjol.charm.charmony.helper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public final class NetworkHelper {
    public static void encodeComponent(FriendlyByteBuf buf, Component component) {
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(buf, component);
    }

    public static Component decodeComponent(FriendlyByteBuf buf) {
        return ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(buf);
    }
}
