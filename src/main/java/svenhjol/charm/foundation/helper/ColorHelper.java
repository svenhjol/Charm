package svenhjol.charm.foundation.helper;

import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;

public final class ColorHelper {
    public static class Color {
        private int color;

        public Color(int color) {
            this.color = color;
        }

        public Color(DyeColor color) {
            this.color = color.getTextureDiffuseColor();
        }

        public float getRed() {
            return (float) FastColor.ARGB32.red(this.color) / 255.0f;
        }

        public float getGreen() {
            return (float)FastColor.ARGB32.green(this.color) / 255.0f;
        }

        public float getBlue() {
            return (float)FastColor.ARGB32.blue(this.color) / 255.0f;
        }

    }
}
