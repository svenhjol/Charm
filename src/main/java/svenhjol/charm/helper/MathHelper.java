package svenhjol.charm.helper;

/**
 * @version 1.0.0-charm
 */
public class MathHelper {
    /**
     * @link {https://stackoverflow.com/a/22186845}
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
