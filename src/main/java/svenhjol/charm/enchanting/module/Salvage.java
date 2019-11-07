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
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING,
    description = "An item with the Salvage enchantment does not disappear when its durability is depleted, giving you a chance to get it repaired.\n" +
        "If the item runs out of durability the player will drop it and must be picked up again. Watch out for lava.")
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

            if (player.world.isRemote) {
                player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.PLAYERS, 0.5F, 1.5F);
            }
        }
    }
}
