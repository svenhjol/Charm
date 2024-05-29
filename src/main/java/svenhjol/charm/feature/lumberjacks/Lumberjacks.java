package svenhjol.charm.feature.lumberjacks;

import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.lumberjacks.common.Advancements;
import svenhjol.charm.feature.lumberjacks.common.Registers;
import svenhjol.charm.feature.lumberjacks.common.Trades;
import svenhjol.charm.feature.woodcutters.Woodcutters;

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
    private static boolean customBarrels = true;

    @Configurable(
        name = "Use custom bookshelves in trades",
        description = """
            If true, lumberjacks will provide custom bookshelves in their trades.
            If false, lumberjacks will trade the vanilla bookshelf."""
    )
    private static boolean customBookshelves = true;

    @Configurable(
        name = "Use custom ladders in trades",
        description = """
            If true, lumberjacks will provide custom ladders in their trades.
            If false, lumberjacks will trade the vanilla ladder."""
    )
    private static boolean customLadders = true;

    public Lumberjacks(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        trades = new Trades(this);
    }

    public boolean customBarrels() {
        return customBarrels;
    }

    public boolean customBookshelves() {
        return customBookshelves;
    }

    public boolean customLadders() {
        return customLadders;
    }


    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> loader().isEnabled(Woodcutters.class));
    }
}
