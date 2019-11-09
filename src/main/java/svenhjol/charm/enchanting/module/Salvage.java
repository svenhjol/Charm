package svenhjol.charm.enchanting.module;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.SalvageEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING,
    description = "An item with the Salvage enchantment drops its enchantments to a book before being destroyed.\n")
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
        if (stack.getDamage() >= stack.getMaxDamage()
            && EnchantmentsHelper.hasEnchantment(enchantment, stack)
            && player != null
        ) {
            if (EnchantmentHelper.getEnchantments(stack).size() > 1) {
                ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    if (entry.getKey().equals(Salvage.enchantment)) continue; // don't add salvage to the book
                    book.addEnchantment(entry.getKey(), entry.getValue());
                }

                PlayerHelper.addOrDropStack(player, book);

                if (!player.world.isRemote) {
                    player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5F, 1.2F);
                }
            }
        }
    }
}
