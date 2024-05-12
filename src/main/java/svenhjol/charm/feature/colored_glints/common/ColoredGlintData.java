package svenhjol.charm.feature.colored_glints.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.foundation.Resolve;

public record ColoredGlintData(DyeColor color) {
    private static final ColoredGlints COLORED_GLINTS = Resolve.feature(ColoredGlints.class);

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
        return stack.has(COLORED_GLINTS.registers.data.get());
    }

    public static ColoredGlintData get(ItemStack stack) {
        return stack.getOrDefault(COLORED_GLINTS.registers.data.get(), EMPTY);
    }

    public static void set(ItemStack stack, Mutable mutable) {
        stack.set(COLORED_GLINTS.registers.data.get(), mutable.toImmutable());
    }

    public static void remove(ItemStack stack) {
        stack.remove(COLORED_GLINTS.registers.data.get());
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
