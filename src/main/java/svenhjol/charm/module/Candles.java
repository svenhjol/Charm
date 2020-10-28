package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.item.BeeswaxItem;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.DecorationHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Candles are made from beeswax and have a lower ambient light than torches.")
public class Candles extends CharmModule {
    public static CandleBlock CANDLE;
    public static BeeswaxItem BEESWAX;

    @Config(name = "Light level", description = "The amount of light (out of 15) that a lit candle will provide.")
    public static int lightLevel = 9;

    @Config(name = "Lit when placed", description = "If true, candles will be lit when placed rather than needing a flint and steel.")
    public static boolean litWhenPlaced = false;

    @Override
    public void register() {
        CANDLE = new CandleBlock(this);
        BEESWAX = new BeeswaxItem(this);
    }

    @Override
    public void init() {
        // add Candles to decoration blocks
        DecorationHelper.DECORATION_BLOCKS.add(CANDLE);
        DecorationHelper.STATE_CALLBACK.put(CANDLE, s -> CANDLE.getDefaultState().with(CandleBlock.LIT, true));
    }
}
