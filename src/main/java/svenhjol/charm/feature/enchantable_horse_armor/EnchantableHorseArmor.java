package svenhjol.charm.feature.enchantable_horse_armor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.iface.IProvidesHorseArmorEnchantments;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.ApiHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Horse armor can be enchanted.")
public class EnchantableHorseArmor extends CharmFeature implements IProvidesHorseArmorEnchantments {
    private static final List<Enchantment> VALIDATED_ENCHANTMENTS = new ArrayList<>();
    
    @Configurable(name = "Enchantments", description = "Enchantments that will function on horse armor.")
    public static List<String> enchantments = Arrays.asList(
        "minecraft:protection",
        "minecraft:fire_protection",
        "minecraft:blast_protection",
        "minecraft:projectile_protection",
        "minecraft:thorns",
        "minecraft:frost_walker",
        "minecraft:feather_falling",
        "minecraft:respiration"
    );

    @Override
    public void register() {
        CharmApi.registerProvider(this);
    }

    @Override
    public List<Enchantment> getEnchantments() {
        if (VALIDATED_ENCHANTMENTS.isEmpty()) {
            // Validate configured enchantments and cache them.
            for (var enchantment : enchantments) {
                BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation(enchantment)).ifPresent(e -> {
                    Charm.instance().log().debug(getClass(), "Adding enchantment " + enchantment);
                    VALIDATED_ENCHANTMENTS.add(e);
                });
            }
        }

        return VALIDATED_ENCHANTMENTS;
    }

    public static List<Enchantment> getAllEnchantments() {
        return ApiHelper.getProviderData(IProvidesHorseArmorEnchantments.class,
                provider -> provider.getEnchantments().stream());
    }
}