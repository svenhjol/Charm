package svenhjol.charm.feature.mob_drops;

import svenhjol.charm.feature.mob_drops.cave_spider_drops.CaveSpiderDrops;
import svenhjol.charm.feature.mob_drops.chicken_drops.ChickenDrops;
import svenhjol.charm.feature.mob_drops.common.DropHandler;
import svenhjol.charm.feature.mob_drops.common.Handlers;
import svenhjol.charm.feature.mob_drops.common.Registers;
import svenhjol.charm.feature.mob_drops.goat_drops.GoatDrops;
import svenhjol.charm.feature.mob_drops.husk_drops.HuskDrops;
import svenhjol.charm.feature.mob_drops.witch_drops.WitchDrops;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature(description = "Some mobs have a chance to drop additional items either by chance or when killed.")
public final class MobDrops extends CommonFeature {
    public static final double LOOTING_MULTIPLIER = 0.1d;

    public final Registers registers;
    public final Handlers handlers;

    public MobDrops(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new CaveSpiderDrops(loader()),
            new ChickenDrops(loader()),
            new GoatDrops(loader()),
            new HuskDrops(loader()),
            new WitchDrops(loader())
        );
    }

    public void registerDropHandler(DropHandler handler) {
        registers.dropHandlers.add(handler);
    }
}
