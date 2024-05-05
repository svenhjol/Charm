package svenhjol.charm.feature.totem_of_preserving;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record TotemData(List<ItemStack> items, String message, boolean glint) {
    public static final Codec<TotemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items")
            .forGetter(TotemData::items),
        Codec.STRING.fieldOf("message")
            .forGetter(TotemData::message),
        Codec.BOOL.fieldOf("glint")
            .forGetter(TotemData::glint)
    ).apply(instance, TotemData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            TotemData::items,
        ByteBufCodecs.STRING_UTF8,
            TotemData::message,
        ByteBufCodecs.BOOL,
            TotemData::glint,
        TotemData::new
    );

    public static final TotemData EMPTY = new TotemData(List.of(), "", false);

    public static Mutable create() {
        return new Mutable(new TotemData(List.of(), "", false));
    }

    public static TotemData get(ItemStack stack) {
        return stack.getOrDefault(TotemOfPreserving.data.get(), EMPTY);
    }

    public static Mutable getMutable(ItemStack stack) {
        return new Mutable(get(stack));
    }

    public static void set(ItemStack stack, Mutable data) {
        stack.set(TotemOfPreserving.data.get(), data.toImmutable());
    }

    public static class Mutable {
        private List<ItemStack> items;
        private String message;
        private boolean glint;

        public Mutable(TotemData data) {
            this.items = data.items();
            this.message = data.message();
            this.glint = data.glint();
        }

        public TotemData toImmutable() {
            return new TotemData(items, message, glint);
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

            if (!items.isEmpty()) {
                this.glint = true;
            }

            return this;
        }
    }
}
