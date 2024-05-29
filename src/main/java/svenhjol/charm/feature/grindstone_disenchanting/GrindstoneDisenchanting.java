package svenhjol.charm.feature.grindstone_disenchanting;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.grindstone_disenchanting.common.Advancements;
import svenhjol.charm.feature.grindstone_disenchanting.common.Handlers;
import svenhjol.charm.feature.grindstone_disenchanting.common.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.BooleanSupplier;

@Feature(description = "Extract enchantments from any enchanted item onto an empty book using the grindstone.")
public final class GrindstoneDisenchanting extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(name = "Initial cost", description = "Initial cost (in XP levels) of extraction before adding on the cost of the enchantment(s).")
    private static int initialCost = 5;

    @Configurable(name = "Treasure cost", description = "Adds extra cost (in XP levels) if the enchantment is a treasure enchantment such as Mending.")
    private static int treasureCost = 5;

    @Configurable(name = "Add item repair cost", description = "If true, the item's repair cost will be added to the cost of extraction.")
    private static boolean addRepairCost = true;

    public GrindstoneDisenchanting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    public int initialCost() {
        return Mth.clamp(initialCost, 0, 30);
    }

    public int treasureCost() {
        return Mth.clamp(treasureCost, 0, 30);
    }

    public boolean addRepairCost() {
        return addRepairCost;
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("grindenchantments"));
    }
}