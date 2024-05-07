package svenhjol.charm.feature.totem_of_preserving.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;

import java.util.List;

public record Data(List<ItemStack> items, String message) {
    public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("items")
            .forGetter(Data::items),
        Codec.STRING.fieldOf("message")
            .forGetter(Data::message)
    ).apply(instance, Data::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            Data::items,
        ByteBufCodecs.STRING_UTF8,
            Data::message,
        Data::new
    );

    public static final Data EMPTY = new Data(List.of(), "");

    public static Mutable create() {
        return new Mutable(EMPTY);
    }

    public static Data get(ItemStack stack) {
        return stack.getOrDefault(TotemOfPreserving.registers.data.get(), EMPTY);
    }

    public static Mutable mutable(ItemStack stack) {
        return new Mutable(get(stack));
    }

    public static void set(ItemStack stack, Mutable mutable) {
        stack.set(TotemOfPreserving.registers.data.get(), mutable.toImmutable());
    }

    public static class Mutable {
        private List<ItemStack> items;
        private String message;

        public Mutable(Data data) {
            this.items = data.items();
            this.message = data.message();
        }

        public Data toImmutable() {
            return new Data(items, message);
        }

        public void save(ItemStack stack) {
            Data.set(stack, this);
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
