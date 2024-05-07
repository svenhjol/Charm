package svenhjol.charm.feature.auto_restock;

import svenhjol.charm.feature.auto_restock.common.Advancements;
import svenhjol.charm.feature.auto_restock.common.Handlers;
import svenhjol.charm.feature.auto_restock.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;

public class AutoRestock extends CommonFeature {
    public static Advancements advancements;
    public static Handlers handlers;
    public static Registers registers;

    @Override
    public String description() {
        return "Refills hotbar from your inventory.";
    }

    @Override
    public void setup() {
        advancements = new Advancements(this);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
