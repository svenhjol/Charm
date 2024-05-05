package svenhjol.charm.feature.colored_glints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public record ColoredGlintData(DyeColor color) {
    public static final Codec<ColoredGlintData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        DyeColor.CODEC.fieldOf("color")
            .forGetter(ColoredGlintData::color)
    ).apply(instance, ColoredGlintData::new));

    public static final StreamCodec<FriendlyByteBuf, ColoredGlintData> STREAM_CODEC = StreamCodec.composite(
        DyeColor.STREAM_CODEC,
            ColoredGlintData::color,
        ColoredGlintData::new
    );

    public static final ColoredGlintData EMPTY = new ColoredGlintData(DyeColor.PURPLE);

    public static Mutable create() {
        return new Mutable(EMPTY);
    }

    public static boolean has(ItemStack stack) {
        return stack.has(ColoredGlints.data.get());
    }

    public static ColoredGlintData get(ItemStack stack) {
        return stack.getOrDefault(ColoredGlints.data.get(), EMPTY);
    }

    public static void set(ItemStack stack, Mutable mutable) {
        stack.set(ColoredGlints.data.get(), mutable.toImmutable());
    }

    public static void remove(ItemStack stack) {
        stack.remove(ColoredGlints.data.get());
    }

    public static class Mutable {
        private DyeColor color;

        public Mutable(ColoredGlintData data) {
            this.color = data.color;
        }

        public Mutable setColor(DyeColor color) {
            this.color = color;
            return this;
        }

        public ColoredGlintData toImmutable() {
            return new ColoredGlintData(color);
        }

        public void save(ItemStack stack) {
            ColoredGlintData.set(stack, this);
        }
    }
}
