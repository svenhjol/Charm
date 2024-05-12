package svenhjol.charm.feature.extractable_enchantments;

import svenhjol.charm.feature.extractable_enchantments.common.Handlers;
import svenhjol.charm.feature.extractable_enchantments.common.Registers;
import svenhjol.charm.feature.extractable_enchantments.common.Advancements;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.util.List;
import java.util.function.BooleanSupplier;

@Feature(description = "Extract enchantments from any enchanted item onto an empty book using the grindstone.")
public class ExtractableEnchantments extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;
    public final Registers registers;

    @Configurable(name = "Initial cost", description = "Initial cost (in XP levels) of extraction before adding on the cost of the enchantment(s).")
    public static int initialCost = 5;

    @Configurable(name = "Treasure cost", description = "Adds extra cost (in XP levels) if the enchantment is a treasure enchantment such as Mending.")
    public static int treasureCost = 5;

    @Configurable(name = "Add item repair cost", description = "If true, the item's repair cost will be added to the cost of extraction.")
    public static boolean addRepairCost = true;

    public ExtractableEnchantments(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("grindenchantments"));
    }
}