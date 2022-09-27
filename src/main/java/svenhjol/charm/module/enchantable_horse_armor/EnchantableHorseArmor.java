package svenhjol.charm.module.enchantable_horse_armor;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Horse armor can be enchanted.")
public class EnchantableHorseArmor extends CharmModule {
    public static final List<Enchantment> VALID_ENCHANTMENTS = new ArrayList<>();

    public static List<String> configValidEnchantments = Arrays.asList(
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
    public void runWhenEnabled() {
        for (String configEnchantment : configValidEnchantments) {
            registerEnchantment(new ResourceLocation(configEnchantment));
        }
    }

    public static void registerEnchantment(ResourceLocation enchantment) {
        Registry.ENCHANTMENT.getOptional(enchantment).ifPresent(e -> {
            LogHelper.debug(EnchantableHorseArmor.class, "Adding enchantment: " + enchantment);
            VALID_ENCHANTMENTS.add(e);
        });
    }
}
