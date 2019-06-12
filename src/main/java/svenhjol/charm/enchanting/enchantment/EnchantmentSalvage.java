package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.feature.Salvage;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.SoundHelper;

import java.util.UUID;

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
            UUID playerId = event.getEntityPlayer().getUniqueID();
            ItemStack original = event.getOriginal();
            ItemStack out = original.copy();

            out.setItemDamage(original.getMaxDamage());

            if (Salvage.ignoreDrops.containsKey(playerId)) {
                // don't drop, just remove the ignore
                ItemStack ignored = Salvage.ignoreDrops.get(playerId);
                Salvage.ignoreDrops.remove(playerId);

                if (ItemStack.areItemStacksEqual(ignored, original)) {
                    return;
                }
            }

            // drop item
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
}