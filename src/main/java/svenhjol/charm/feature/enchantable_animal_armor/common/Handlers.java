package svenhjol.charm.feature.enchantable_animal_armor.common;

import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.feature.FeatureHolder;

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
}
