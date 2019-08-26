package svenhjol.charm.enchanting.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.SalvageEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING)
public class Salvage extends MesonModule
{
    public static SalvageEnchantment enchantment;

    @Override
    public void init()
    {
        enchantment = new SalvageEnchantment(this);
    }

    public static void itemDamaged(ItemStack stack, int amount, @Nullable ServerPlayerEntity player)
    {
        ItemStack copy = stack.copy();

        if (copy.getDamage() >= copy.getMaxDamage()
            && EnchantmentsHelper.hasEnchantment(enchantment, copy)
            && player != null
        ) {
            copy.setDamage(stack.getMaxDamage());
            player.dropItem(copy, false);
            SoundHelper.playSoundAtPos(player, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5F, 1.5F);
        }
    }
}
