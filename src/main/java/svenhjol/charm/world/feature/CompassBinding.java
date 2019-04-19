package svenhjol.charm.world.feature;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.world.item.ItemBoundCompass;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.PlayerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompassBinding extends Feature
{
    public static ItemBoundCompass beaconCompass;
    public static List<Item> validBeaconCompasses = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        beaconCompass = new ItemBoundCompass("beacon_compass");
        validBeaconCompasses.add(Items.COMPASS);
        validBeaconCompasses.add(beaconCompass);
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
            if (!validBeaconCompasses.contains(player.getHeldItem(event.getHand()).getItem())) return; // must be holding a vanilla beaconCompass

            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EnumHand hand = event.getHand();

            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityBeacon) {
                TileEntityBeacon beacon = (TileEntityBeacon) tile;

                // there might be a better way to detect active beacon?
                if (beacon.getLevels() < 1) return;

                // get glass above the beacon
                int meta = 0;

                for (int i = 256-pos.getY(); i > 0; i--) {
                    IBlockState above = world.getBlockState(pos.offset(EnumFacing.UP, i));
                    if (above.getBlock() == Blocks.STAINED_GLASS) {
                        meta = above.getValue(BlockStainedGlass.COLOR).getDyeDamage();
                        break;
                    }
                }

                // create the new compass and set the player to hold it or in their inventory
                ItemStack compass = new ItemStack(beaconCompass);
                BlockPos compassPos = new BlockPos(pos.getX(), world.provider.getDimension(), pos.getZ());
                String name = Objects.requireNonNull(beacon.getDisplayName()).getUnformattedText();
                if (name.length() > 0) {
                    //noinspection deprecation
                    compass.setStackDisplayName(name + " " + I18n.translateToLocal("compass")); // I18n.translateToLocal is deprecated
                }
                ItemBoundCompass.setPos(compass, compassPos);
                ItemBoundCompass.setColor(compass, meta);
                PlayerHelper.setHeldItem(player, hand, compass);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
