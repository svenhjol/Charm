package svenhjol.charm.module.collection_enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.enchantment.CharmEnchantment;
import svenhjol.charm.loader.CharmModule;

public class CollectionEnch extends CharmEnchantment {
    public CollectionEnch(CharmModule module) {
        super(module, "collection", Rarity.UNCOMMON, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
    }

    @Override
    public int getMinCost(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return module.isEnabled() && (stack.getItem() instanceof ShearsItem || super.canEnchant(stack));
    }
}
