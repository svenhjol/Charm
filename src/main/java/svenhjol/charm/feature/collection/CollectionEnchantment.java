package svenhjol.charm.feature.collection;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charmony.base.CharmonyEnchantment;
import svenhjol.charmony.base.CharmonyFeature;

public class CollectionEnchantment extends CharmonyEnchantment {
    public CollectionEnchantment(CharmonyFeature feature) {
        super(feature, Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND});
    }
    
    @Override
    public int getMinCost(int level) {
        return 15;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return feature.isEnabled() && (stack.getItem() instanceof ShearsItem || super.canEnchant(stack));
    }
}