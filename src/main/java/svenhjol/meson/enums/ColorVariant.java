package svenhjol.meson.enums;

import svenhjol.meson.iface.IMesonEnum;

import java.util.Random;

public enum ColorVariant implements IMesonEnum
{
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    SILVER,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    private static final ColorVariant[] METADATA_LOOKUP = new ColorVariant[values().length];

    static {
        ColorVariant[] values = values();
        for (ColorVariant v : values) {
            METADATA_LOOKUP[v.ordinal()] = v;
        }
    }

    public static ColorVariant byMeta(int meta)
    {
        if (meta < 0 || meta >= METADATA_LOOKUP.length) {
            meta = 0;
        }
        return METADATA_LOOKUP[meta];
    }

    public static ColorVariant random()
    {
        return byMeta(new Random().nextInt(values().length));
    }
}
