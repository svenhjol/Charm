package svenhjol.charm.feature.pigs_find_mushrooms;

import svenhjol.charm.feature.pigs_find_mushrooms.common.Advancements;
import svenhjol.charm.feature.pigs_find_mushrooms.common.Handlers;
import svenhjol.charm.feature.pigs_find_mushrooms.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Pigs have a chance to find mushrooms from certain blocks. By default mushrooms are unearthed from mycelium and podzol blocks.
    The item tag 'pigs_find_mushrooms' can be used to configure the blocks in which mushrooms can be unearthed by a pig.""")
public class PigsFindMushrooms extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(
        name = "Chance to find mushroom",
        description = "Approximately 1 in X chance of a pig finding a mushroom per game tick.",
        requireRestart = false
    )
    public static int findChance = 1000;

    @Configurable(
        name = "Chance to erode block",
        description = "Chance (out of 1.0) of a block being converted to dirt when a pig finds a mushroom.",
        requireRestart = false
    )
    public static double erodeChance = 0.25d;

    public PigsFindMushrooms(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
