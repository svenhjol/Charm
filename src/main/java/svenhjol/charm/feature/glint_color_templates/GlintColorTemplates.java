package svenhjol.charm.feature.glint_color_templates;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.glint_color_templates.common.Advancements;
import svenhjol.charm.feature.glint_color_templates.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Smithing template that changes the glint color of any enchanted item.")
public final class GlintColorTemplates extends CommonFeature {
    public final Registers registers;
    public final Advancements advancements;

    @Configurable(
        name = "Loot table",
        description = "Loot table in which a colored glint smithing template will be added."
    )
    private static String lootTable = "minecraft:chests/stronghold_library";

    @Configurable(
        name = "Loot chance",
        description = "Chance (out of 1.0) of a colored glint smithing template appearing in loot."
    )
    private static double lootChance = 1.0d;

    public GlintColorTemplates(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
    }

    public String lootTable() {
        return lootTable;
    }

    public double lootChance() {
        return Mth.clamp(lootChance, 0.0d, 1.0d);
    }
}
