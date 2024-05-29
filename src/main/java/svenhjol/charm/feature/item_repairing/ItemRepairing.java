package svenhjol.charm.feature.item_repairing;

import svenhjol.charm.feature.item_repairing.common.Handlers;
import svenhjol.charm.feature.item_repairing.common.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "More ways to repair items using different materials.")
public final class ItemRepairing extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(name = "Repair tridents using prismarine", description = "Use prismarine shards to repair trident damage.")
    private static boolean repairTridents = true;

    @Configurable(name = "Repair elytra using leather", description = "Leather can be used to repair elytra when insomnia is disabled.")
    private static boolean repairElytra = true;

    @Configurable(name = "Repair netherite using scrap", description = "Use netherite scrap to repair netherite item damage.")
    private static boolean repairNetheriteItems = true;

    public ItemRepairing(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public boolean repairElytra() {
        return repairElytra;
    }

    public boolean repairNetheriteItems() {
        return repairNetheriteItems;
    }

    public boolean repairTridents() {
        return repairTridents;
    }
}
