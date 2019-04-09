package svenhjol.meson.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PlayerHelper
{
    public static List<String> getBiomeTypes(EntityPlayer player)
    {
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(player.world.getBiome(player.getPosition()));
        List<String> actualTypes = new ArrayList<>();
        types.forEach(type -> actualTypes.add(type.getName()));
        return actualTypes;
    }

    @SuppressWarnings("unused")
    public static List<NonNullList<ItemStack>> getAllInventories(EntityPlayer player)
    {
        InventoryPlayer inventory = player.inventory;
        return Arrays.asList(inventory.mainInventory, inventory.armorInventory, inventory.offHandInventory);
    }

    public static double getLocationAngleToPlayer(EntityPlayer player, BlockPos location)
    {
        return Math.atan2((double)location.getZ() - player.posZ, (double)location.getX() - player.posX);
    }

    @SuppressWarnings("unused")
    public static void replaceHeldItem(EntityPlayer player, EnumHand hand, ItemStack item)
    {
        player.setHeldItem(hand, item);
    }

    public static void setHeldItem(EntityPlayer player, EnumHand hand, ItemStack item)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() == 1) {
            player.setHeldItem(hand, item);
        } else {
            stack.shrink(1);
            if (stack.getCount() == 0) {
                player.setHeldItem(hand, item);
            } else {
                addOrDropStack(player, item);
            }
        }
    }

    public static void addOrDropStack(EntityPlayer player, ItemStack item)
    {
        if (!player.inventory.addItemStackToInventory(item)) {
            player.dropItem(item, false);
        }
    }

    /**
     * Basic way to teleport a player to co-ordinate in a dimension.
     * If the player is not in the specified dimension they will be transferred first.
     */
    public static void teleportPlayer(EntityPlayer player, BlockPos pos, int dimension)
    {
        if (player.getEntityWorld().provider.getDimension() != dimension) {
            MinecraftServer server = player.world.getMinecraftServer();
            if (server != null) {
                transferPlayerToDimension((EntityPlayerMP) player, dimension, server.getPlayerList());
            }
        }

        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    /**
     * From CoFH EntityHelper::transferPlayerToDimension
     * @link {https://github.com/CoFH/CoFHCore/blob/1.12/src/main/java/cofh/core/util/helpers/EntityHelper.java}
     */
    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, PlayerList manager)
    {
        int oldDim = player.dimension;
        WorldServer worldserver = manager.getServerInstance().getWorld(player.dimension);
        player.dimension = dimension;
        WorldServer worldserver1 = manager.getServerInstance().getWorld(player.dimension);
        player.connection.sendPacket(new SPacketRespawn(player.dimension, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
        worldserver.removeEntityDangerously(player);
        if (player.isBeingRidden()) {
            player.removePassengers();
        }
        if (player.isRiding()) {
            player.dismountRidingEntity();
        }
        player.isDead = false;
        EntityHelper.transferEntityToWorld(player, worldserver, worldserver1);
        manager.preparePlayer(player, worldserver);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(player, worldserver1);
        manager.syncPlayerInventory(player);

        for (PotionEffect potioneffect : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }
}
