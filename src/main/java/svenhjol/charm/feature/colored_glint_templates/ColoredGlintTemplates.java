package svenhjol.charm.feature.colored_glint_templates;

import svenhjol.charm.feature.colored_glint_templates.common.Advancements;
import svenhjol.charm.feature.colored_glint_templates.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Smithing template that changes the glint color of any enchanted item.")
public final class ColoredGlintTemplates extends CommonFeature {
    public final Registers registers;
    public final Advancements advancements;

    @Configurable(
        name = "Loot table",
        description = "Loot table in which a colored glint smithing template will be added."
    )
    public static String configurableLootTable = "minecraft:chests/stronghold_library";

    @Configurable(
        name = "Loot chance",
        description = "Chance (out of 1.0) of a colored glint smithing template appearing in loot."
    )
    public static double lootChance = 1.0d;

    public ColoredGlintTemplates(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
    }
}
