package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEnum;

import java.util.Random;

public abstract class MesonBlock extends Block implements IMesonBlock
{
    protected String name;

    public MesonBlock(Material material, String name)
    {
        super(material);
        this.register(name);
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public enum WoodVariant implements IMesonEnum
    {
        OAK,
        SPRUCE,
        BIRCH,
        JUNGLE,
        ACACIA,
        DARK_OAK;

        private static final WoodVariant[] METADATA_LOOKUP = new WoodVariant[values().length];

        static {
            WoodVariant[] values = values();
            for (WoodVariant v : values) {
                METADATA_LOOKUP[v.ordinal()] = v;
            }
        }

        public static WoodVariant byMetadata(int meta)
        {
            if (meta < 0 || meta >= METADATA_LOOKUP.length) {
                meta = 0;
            }
            return METADATA_LOOKUP[meta];
        }

        public static WoodVariant random()
        {
            return byMetadata(new Random().nextInt(values().length));
        }
    }
}