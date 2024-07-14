package svenhjol.charm.feature.animal_armor_enchanting.common;

import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;

public final class Handlers extends FeatureHolder<AnimalArmorEnchanting> {
    public Handlers(AnimalArmorEnchanting feature) {
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
            && feature().registers.wolfArmorEnchantments.contains(enchantment);
    }
}
