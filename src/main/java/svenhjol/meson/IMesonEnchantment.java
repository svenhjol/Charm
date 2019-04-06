package svenhjol.meson;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.helper.EnchantmentHelper;

public interface IMesonEnchantment
{
    String getModId();

    default void register(String name)
    {
        Enchantment self = (Enchantment)this;
        self.setName(name);
        self.setRegistryName(new ResourceLocation(getModId(), name));
        ProxyRegistry.register(self);
    }

    default boolean isHeldItemEnchanted(EntityPlayer player, Enchantment enchantment, EnumHand hand)
    {
        return (!(player.getHeldItem(hand).isEmpty()) && EnchantmentHelper.hasEnchantment(enchantment, player.getHeldItem(hand)));
    }

    default boolean isHeldItemEnchanted(EntityPlayer player, Enchantment enchantment)
    {
        return (isHeldItemEnchanted(player, enchantment, EnumHand.MAIN_HAND)
            || isHeldItemEnchanted(player, enchantment, EnumHand.OFF_HAND));
    }

    interface ICurse
    {
    }
}
