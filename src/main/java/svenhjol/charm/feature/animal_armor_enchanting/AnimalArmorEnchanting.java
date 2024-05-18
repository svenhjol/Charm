package svenhjol.charm.feature.animal_armor_enchanting;

import svenhjol.charm.feature.animal_armor_enchanting.common.Advancements;
import svenhjol.charm.feature.animal_armor_enchanting.common.Handlers;
import svenhjol.charm.feature.animal_armor_enchanting.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Horse and wolf armor can be enchanted.
    The enchantment tags 'on_horse_armor' and 'on_wolf_armor' can be used to configure valid enchantments.""")
public class AnimalArmorEnchanting extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public AnimalArmorEnchanting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}