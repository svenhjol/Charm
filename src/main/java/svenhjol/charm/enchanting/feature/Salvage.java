package svenhjol.charm.enchanting.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.enchanting.enchantment.EnchantmentSalvage;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.SoundHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Salvage extends Feature
{
    public static EnchantmentSalvage enchantment;
    public static int minEnchantability;
    public static Map<UUID, ItemStack> ignoreEquipped = new HashMap<>();

    @Override
    public String getDescription()
    {
        return "An item with the Salvage enchantment does not disappear when its durability is depleted, giving you a chance to get it repaired.\n" +
                "If the item runs out of durability the player will drop it and must be picked up again.  Watch out for lava.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        minEnchantability = 5;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        enchantment = new EnchantmentSalvage();
    }

    // prevents armor equipping from dropping a duplicate
    @SubscribeEvent
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event)
    {
        if (!event.getWorld().isRemote
            && event.getEntityPlayer() != null
            && event.getItemStack().getItem() instanceof ItemArmor // might need to expand this to other equippable things
            && EnchantmentHelper.hasEnchantment(enchantment, event.getItemStack())
        ) {
            ignoreEquipped.put(event.getEntityPlayer().getUniqueID(), event.getItemStack().copy());
        }
    }

    // allows tool to drop if it reaches zero durability
    @SubscribeEvent
    public void onDestroy(PlayerDestroyItemEvent event)
    {
        if (!event.getEntityPlayer().world.isRemote
            && EnchantmentHelper.hasEnchantment(enchantment, event.getOriginal())
        ) {
            EntityPlayer player = event.getEntityPlayer();
            UUID playerId = player.getUniqueID();
            ItemStack item = event.getOriginal();

            if (ignoreEquipped.containsKey(playerId)) {
                // don't drop, just remove the ignore
                ItemStack ignored = ignoreEquipped.get(playerId);
                ignoreEquipped.remove(playerId);
                if (ItemStack.areItemStacksEqual(ignored, item)) return;
            }

            dropItem(player, item);
        }
    }

    public static void dropItem(EntityPlayer player, ItemStack stack)
    {
        stack.setItemDamage(stack.getMaxDamage());
        player.dropItem(stack, false);
        SoundHelper.playerSound(player, SoundEvents.BLOCK_ANVIL_LAND, 0.5f, 1.5f, 0.15f, null);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
