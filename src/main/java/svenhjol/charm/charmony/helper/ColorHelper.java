package svenhjol.charm.charmony.helper;

import net.minecraft.world.item.DyeColor;

public final class ColorHelper {
    public static class Color {
        private final float[] textureDiffuseColors;
        
        public Color(DyeColor color) {
            this.textureDiffuseColors = color.getTextureDiffuseColors();
        }

        public float getRed() {
            return textureDiffuseColors[0];
        }

        public float getGreen() {
            return textureDiffuseColors[1];
        }

        public float getBlue() {
            return textureDiffuseColors[2];
        }
    }
}
