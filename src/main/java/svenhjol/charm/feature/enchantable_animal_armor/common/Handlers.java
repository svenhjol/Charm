package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.Tags;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.TagHelper;

public final class Handlers extends FeatureHolder<EnchantableAnimalArmor> {
    public Handlers(EnchantableAnimalArmor feature) {
        super(feature);
    }

    /**
     * Called by mixin.
     * Check if the itemstack can be enchanted with the given enchantment.
     * The itemstack should always be checked to see if it's horse armor.
     */
    public boolean isValidHorseEnchantment(ItemStack stack, Enchantment enchantment) {
        return stack.getItem() instanceof AnimalArmorItem armor
            && armor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN
            && feature().registers.horseArmorEnchantments.contains(enchantment);
    }

    /**
     * Called by mixin.
     * Check if the itemstack can be enchanted with the given enchantment.
     * The itemstack should always be checked to see if it's wolf armor.
     */
    public boolean isValidWolfEnchantment(ItemStack stack, Enchantment enchantment) {
        return stack.getItem() instanceof AnimalArmorItem armor
            && armor.getBodyType() == AnimalArmorItem.BodyType.CANINE
            && feature().registers.horseArmorEnchantments.contains(enchantment);
    }

    /**
     * Called when the world loads to fetch the tag contents and populate them.
     */
    public void levelLoad(MinecraftServer server, ServerLevel level) {
        var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        // Load horse armor from the tag and cache it in our registry.
        feature().registers.horseArmorEnchantments.clear();
        feature().registers.horseArmorEnchantments.addAll(TagHelper.getValues(registry, Tags.ON_HORSE_ARMOR));

        // Load wolf armor from the tag and cache it in our registry.
        feature().registers.wolfArmorEnchantments.clear();
        feature().registers.wolfArmorEnchantments.addAll(TagHelper.getValues(registry, Tags.ON_WOLF_ARMOR));
    }
}
