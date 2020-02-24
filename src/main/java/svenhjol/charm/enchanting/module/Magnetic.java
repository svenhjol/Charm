package svenhjol.charm.enchanting.module;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.MagneticEnchantment;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true,
    description = "Tools with the Magnetic enchantment automatically pick up drops.")
public class Magnetic extends MesonModule
{
    public static int range = 6;
    public static Map<BlockPos, PlayerEntity> dropmap = new HashMap<>();
    public static MesonEnchantment enchantment;

    @Override
    public void init()
    {
        enchantment = new MagneticEnchantment(this);
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event)
    {
        if (EnchantmentsHelper.hasEnchantment(Magnetic.enchantment, event.getPlayer().getHeldItemMainhand())) {
            dropmap.put(event.getPos(), event.getPlayer());
        } else {
            dropmap.values().remove(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onEntityCreate(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof ItemEntity && !event.getWorld().isRemote) {
            int r = range;
            BlockPos foundPos = null;

            for (BlockPos pos : dropmap.keySet()) {
                double dist = WorldHelper.getDistanceSq(pos, event.getEntity().getPosition());
                if (dist <= r) {
                    foundPos = pos;
                    break;
                }
            }

            if (foundPos != null) {
                PlayerEntity player = dropmap.get(foundPos);
                BlockPos playerPos = player.getPosition();
                ItemEntity fake = new ItemEntity(player.world, playerPos.getX(), playerPos.getY(), playerPos.getZ());
                ItemStack item = ((ItemEntity) event.getEntity()).getItem();
                fake.setItem(item);
                dropmap.remove(foundPos);

                if (!MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(player, fake))) {
                    if (player.inventory.addItemStackToInventory(item)) {
                        player.world.playSound(null, foundPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,0.5F, ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        event.setCanceled(true);
                    }
                }

            }
        }
    }
}
