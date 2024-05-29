package svenhjol.charm.feature.animal_armor_enchanting.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.charmony.common.helper.TagHelper;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<AnimalArmorEnchanting> {
    public final List<Enchantment> horseArmorEnchantments = new ArrayList<>();
    public final List<Enchantment> wolfArmorEnchantments = new ArrayList<>();

    public Registers(AnimalArmorEnchanting feature) {
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
