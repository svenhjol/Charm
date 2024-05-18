package svenhjol.charm.feature.lumberjacks;

import svenhjol.charm.feature.lumberjacks.common.Advancements;
import svenhjol.charm.feature.lumberjacks.common.Registers;
import svenhjol.charm.feature.lumberjacks.common.Trades;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

import java.util.List;
import java.util.function.BooleanSupplier;

@Feature(priority = -1, description = "Lumberjacks are villagers that trade wooden items. Their job site is the woodcutter.")
public final class Lumberjacks extends CommonFeature {
    public static final String PROFESSION_ID = "lumberjack";

    public final Registers registers;
    public final Advancements advancements;
    public final Trades trades;

    @Configurable(
        name = "Use custom barrels in trades",
        description = """
            If true, lumberjacks will provide custom barrels in their trades.
            If false, lumberjacks will trade the vanilla barrel."""
    )
    public static boolean customBarrels = true;

    @Configurable(
        name = "Use custom bookshelves in trades",
        description = """
            If true, lumberjacks will provide custom bookshelves in their trades.
            If false, lumberjacks will trade the vanilla bookshelf."""
    )
    public static boolean customBookshelves = true;

    @Configurable(
        name = "Use custom ladders in trades",
        description = """
            If true, lumberjacks will provide custom ladders in their trades.
            If false, lumberjacks will trade the vanilla ladder."""
    )
    public static boolean customLadders = true;

    public Lumberjacks(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        trades = new Trades(this);
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> loader().isEnabled(Woodcutters.class));
    }
}
