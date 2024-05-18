package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.helper.TagHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<EnchantableAnimalArmor> {
    public final List<Enchantment> horseArmorEnchantments = new ArrayList<>();
    public final List<Enchantment> wolfArmorEnchantments = new ArrayList<>();

    public Registers(EnchantableAnimalArmor feature) {
        super(feature);
    }

    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        // Load horse armor from the tag and cache it in our registry.
        horseArmorEnchantments.clear();
        horseArmorEnchantments.addAll(TagHelper.getValues(registry, Tags.ON_HORSE_ARMOR));

        // Load wolf armor from the tag and cache it in our registry.
        wolfArmorEnchantments.clear();
        wolfArmorEnchantments.addAll(TagHelper.getValues(registry, Tags.ON_WOLF_ARMOR));
    }
}
