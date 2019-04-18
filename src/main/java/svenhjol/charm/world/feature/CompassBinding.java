package svenhjol.charm.world.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.world.item.ItemBoundCompass;
import svenhjol.meson.Feature;

public class CompassBinding extends Feature
{
    public static ItemBoundCompass beaconCompass;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        beaconCompass = new ItemBoundCompass("beacon_compass");
    }

    @SubscribeEvent
    public void onCompassUse(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote
            && event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.BEACON
            && event.getEntityPlayer() != null
            && event.getEntityPlayer().isSneaking()
        ) {
            EntityPlayer player = event.getEntityPlayer();
            if (player.getHeldItem(event.getHand()).getItem() != Items.COMPASS) return; // must be holding a vanilla beaconCompass

            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EnumHand hand = event.getHand();

            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityBeacon) {
                TileEntityBeacon beacon = (TileEntityBeacon) tile;

                // there might be a better way to detect active beacon?
                if (beacon.getLevels() < 1) return;
                ItemStack compass = new ItemStack(beaconCompass);
                BlockPos compassPos = new BlockPos(pos.getX(), world.provider.getDimension(), pos.getZ());
                ItemBoundCompass.setPos(compass, compassPos);
                player.setHeldItem(hand, compass);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
