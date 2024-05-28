package svenhjol.charm.feature.totem_of_preserving.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.Resolve;

import java.util.List;

public record TotemData(List<ItemStack> items, String message) {
    private static final TotemOfPreserving TOTEM_OF_PRESERVING = Resolve.feature(TotemOfPreserving.class);

    public static final Codec<TotemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items")
            .forGetter(TotemData::items),
        Codec.STRING.fieldOf("message")
            .forGetter(TotemData::message)
    ).apply(instance, TotemData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            TotemData::items,
        ByteBufCodecs.STRING_UTF8,
            TotemData::message,
        TotemData::new
    );

    public static final TotemData EMPTY = new TotemData(List.of(), "");

    public static Mutable create() {
        return new Mutable(EMPTY);
    }

    public static TotemData get(ItemStack stack) {
        return stack.getOrDefault(TOTEM_OF_PRESERVING.registers.data.get(), EMPTY);
    }

    public static Mutable mutable(ItemStack stack) {
        return new Mutable(get(stack));
    }

    public static void set(ItemStack stack, Mutable mutable) {
        stack.set(TOTEM_OF_PRESERVING.registers.data.get(), mutable.toImmutable());
    }

    public static class Mutable {
        private List<ItemStack> items;
        private String message;

        public Mutable(TotemData data) {
            this.items = data.items();
            this.message = data.message();
        }

        public TotemData toImmutable() {
            return new TotemData(items, message);
        }

        public void save(ItemStack stack) {
            TotemData.set(stack, this);
        }

        public Mutable setMessage(String message) {
            this.message = message;
            return this;
        }

        public Mutable setItems(List<ItemStack> items) {
            this.items = items;
            return this;
        }
    }
}
