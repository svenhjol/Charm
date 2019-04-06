package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.charm.enchanting.feature.CurseBreak;

public class EnchantmentCurseBreak extends MesonEnchantment
{
    public EnchantmentCurseBreak()
    {
        super("curse_break", Rarity.RARE, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() == Items.BOOK;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (!in.isEmpty()
                && !combine.isEmpty()
                && combine.getItem() == Items.ENCHANTED_BOOK
                && EnchantmentHelper.hasEnchantment(this, combine)
        ) {
            ItemStack out = in.copy();
            String name = out.getDisplayName();
            EnchantmentHelper.removeRandomCurse(out);

            if (!name.isEmpty()) {
                out.setStackDisplayName(name);
            }

            event.setCost(CurseBreak.xpCost);
            event.setOutput(out);
        }
    }
}
