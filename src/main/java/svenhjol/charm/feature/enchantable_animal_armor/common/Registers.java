package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<EnchantableAnimalArmor> {
    public final List<Enchantment> horseArmorEnchantments = new ArrayList<>();
    public final List<Enchantment> wolfArmorEnchantments = new ArrayList<>();

    public Registers(EnchantableAnimalArmor feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        LevelLoadEvent.INSTANCE.handle(feature().handlers::levelLoad);
    }
}
