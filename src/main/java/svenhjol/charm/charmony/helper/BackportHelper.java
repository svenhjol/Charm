package svenhjol.charm.charmony.helper;

import net.minecraft.util.Mth;

/**
 * Silly, but we can sort out these missing methods later once it builds.
 */
public final class BackportHelper {
    /**
     * This method is missing from the Mth class.
     */
    public static int lerpDiscrete(float f, int i, int j) {
        int k = j - i;
        return i + Mth.floor(f * (float)(k - 1)) + (f > 0.0f ? 1 : 0);
    }
}