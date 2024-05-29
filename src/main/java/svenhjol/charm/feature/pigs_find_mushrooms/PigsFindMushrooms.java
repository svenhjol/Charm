package svenhjol.charm.feature.pigs_find_mushrooms;

import net.minecraft.util.Mth;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.pigs_find_mushrooms.common.Advancements;
import svenhjol.charm.feature.pigs_find_mushrooms.common.Handlers;
import svenhjol.charm.feature.pigs_find_mushrooms.common.Registers;

@Feature(description = """
    Pigs have a chance to find mushrooms from certain blocks. By default mushrooms are unearthed from mycelium and podzol blocks.
    The item tag 'pigs_find_mushrooms' can be used to configure the blocks in which mushrooms can be unearthed by a pig.""")
public final class PigsFindMushrooms extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(
        name = "Chance to find mushroom",
        description = "Approximately 1 in X chance of a pig finding a mushroom per game tick.",
        requireRestart = false
    )
    private static int findChance = 1000;

    @Configurable(
        name = "Chance to erode block",
        description = "Chance (out of 1.0) of a block being converted to dirt when a pig finds a mushroom.",
        requireRestart = false
    )
    private static double erodeChance = 0.25d;

    public PigsFindMushrooms(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    public int findChance() {
        return Mth.clamp(findChance, 100, 20000);
    }

    public double erodeChance() {
        return Mth.clamp(erodeChance, 0.0d, 1.0f);
    }
}
