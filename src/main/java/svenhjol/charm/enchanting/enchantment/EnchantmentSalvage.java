package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.charm.enchanting.feature.Salvage;

public class EnchantmentSalvage extends MesonEnchantment
{
    public EnchantmentSalvage()
    {
        super("salvage", Rarity.COMMON, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public void onDestroy(PlayerDestroyItemEvent event)
    {
        if (!event.getEntityPlayer().world.isRemote
            && EnchantmentHelper.hasEnchantment(this, event.getOriginal())
        ) {
            ItemStack original = event.getOriginal();
            ItemStack out = event.getOriginal();

            out.setItemDamage(original.getMaxDamage());
            event.getEntityPlayer().dropItem(out, false);

            SoundHelper.playerSound(event.getEntityPlayer(), SoundEvents.BLOCK_ANVIL_LAND, 0.5f, 1.5f, 0.15f, null);
        }
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return Salvage.minEnchantability;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench)
    {
        return !(ench instanceof EnchantmentMending) && super.canApplyTogether(ench);
    }
}