package svenhjol.charm.module;

import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.item.BeeswaxItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Candles are made from beeswax and have a lower ambient light than torches.")
public class Candles extends MesonModule {
    public static CandleBlock CANDLE;
    public static BeeswaxItem BEESWAX;

    @Config(name = "Light level", description = "The amount of light (out of 15) that a lit candle will provide.")
    public static int lightLevel = 9;

    @Config(name = "Lit when placed", description = "If true, candles will be lit when placed rather than needing a flint and steel.")
    public static boolean litWhenPlaced = false;

    @Override
    public void init() {
        CANDLE = new CandleBlock(this);
        BEESWAX = new BeeswaxItem(this);
    }
}
