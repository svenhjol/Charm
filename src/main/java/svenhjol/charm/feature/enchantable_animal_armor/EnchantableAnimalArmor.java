package svenhjol.charm.feature.enchantable_animal_armor;

import svenhjol.charm.feature.enchantable_animal_armor.common.Advancements;
import svenhjol.charm.feature.enchantable_animal_armor.common.Handlers;
import svenhjol.charm.feature.enchantable_animal_armor.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Horse and wolf armor can be enchanted.
    The enchantment tags 'on_horse_armor' and 'on_wolf_armor' can be used to configure valid enchantments.""")
public class EnchantableAnimalArmor extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public EnchantableAnimalArmor(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}