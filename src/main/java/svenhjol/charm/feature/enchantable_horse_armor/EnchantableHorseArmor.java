package svenhjol.charm.feature.enchantable_horse_armor;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.iface.IHorseArmorEnchantmentProvider;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Horse armor can be enchanted.")
public class EnchantableHorseArmor extends CharmFeature implements IHorseArmorEnchantmentProvider {
    static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();
    
    @Configurable(name = "Enchantments", description = "Enchantments that will function on horse armor.")
    public static List<String> configEnchantments = Arrays.asList(
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
        ApiHelper.consume(IHorseArmorEnchantmentProvider.class,
            provider -> provider.getEnchantments().forEach(this::addEnchantment));

        CharmonyApi.registerProvider(this);
    }

    private void addEnchantment(Enchantment enchantment) {
        if (!ENCHANTMENTS.contains(enchantment)) {
            Charm.instance().log().debug(getClass(), "Adding enchantment " + enchantment);
            ENCHANTMENTS.add(enchantment);
        }
    }

    /**
     * Resolves config enchantments into registered enchantments.
     * @return List of resolved enchantments.
     */
    @Override
    public List<Enchantment> getEnchantments() {
        List<Enchantment> validatedEnchantments = new ArrayList<>();

        // Validate configured enchantments and cache them.
        for (var enchantment : configEnchantments) {
            BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation(enchantment))
                .ifPresent(validatedEnchantments::add);
        }

        return validatedEnchantments;
    }

    /**
     * Used by the mixin to fetch the total set of possible horse armor enchantments.
     * @return Complete list of horse armor enchantments.
     */
    public static List<Enchantment> getAllEnchantments() {
        return ENCHANTMENTS;
    }
}