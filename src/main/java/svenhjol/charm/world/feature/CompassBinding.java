package svenhjol.charm.world.feature;

import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
    public static ItemBoundCompass boundCompass;
    public static List<Item> validItems = new ArrayList<>();

    public static boolean useBeacons;
    public static boolean useBeds;
    public static boolean useBanners;
    public static boolean showInformation;


    @Override
    public void setupConfig()
    {
        showInformation = true || propBoolean(
                "Add compass information",
                "If true, adds co-ordinates and dimension to the bound compass tooltip.",
                true
        );
        useBeacons = propBoolean(
                "Use Beacons",
                "If true, compasses can bind to active beacons.",
                true
        );
        useBeds = true || propBoolean(
                "Use Beds",
                "If true, compasses can bind to beds.",
                false
        );
        useBanners = true || propBoolean(
                "Use Banners",
                "If true, compasses can bind to banners.",
                false
        );

    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        boundCompass = new ItemBoundCompass();
        validItems.add(Items.COMPASS);
        validItems.add(boundCompass);
    }

    @SubscribeEvent
    public void onCompassUse(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote
            && event.getEntityPlayer() != null
            && event.getEntityPlayer().isSneaking()
            && validItems.contains(event.getEntityPlayer().getHeldItem(event.getHand()).getItem())
        ) {
            EntityPlayer player = event.getEntityPlayer();
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EnumHand hand = event.getHand();

            boolean validCompass = false;
            int color = 0;
            String name = "";
            ItemStack compass = new ItemStack(boundCompass);
            TileEntity tile = world.getTileEntity(pos);

            if (useBeacons && tile instanceof TileEntityBeacon) {
                TileEntityBeacon beacon = (TileEntityBeacon) tile;

                if (beacon.getLevels() < 1) return; // there might be a better way to detect active beacon?

                // get glass above the beacon
                for (int i = 256 - pos.getY(); i > 0; i--) {
                    IBlockState above = world.getBlockState(pos.offset(EnumFacing.UP, i));
                    if (above.getBlock() == Blocks.STAINED_GLASS) {
                        color = above.getValue(BlockStainedGlass.COLOR).getDyeDamage();
                        break;
                    }
                }

                name = Objects.requireNonNull(beacon.getDisplayName()).getUnformattedText();
                validCompass = true;
            }

            if (useBeds && tile instanceof TileEntityBed) {
                TileEntityBed bed = (TileEntityBed) tile;
                color = bed.getColor().getDyeDamage();
                if (bed.getDisplayName() != null) {
                    name = bed.getDisplayName().getUnformattedText();
                }
                validCompass = true;
            }

            if (useBanners && tile instanceof TileEntityBanner) {
                TileEntityBanner banner = (TileEntityBanner) tile;
                List<EnumDyeColor> colorList = banner.getColorList();
                EnumDyeColor c = colorList.iterator().next();
                color = c.getDyeDamage();

                if (banner.getDisplayName() != null) {
                    name = banner.getDisplayName().getUnformattedText();
                }
                validCompass = true;
            }

            if (!validCompass) return;

            // set up the new compass details
            BlockPos compassPos = new BlockPos(pos.getX(), world.provider.getDimension(), pos.getZ());
            name = (name.length()) > 0 ? name : "Bound Compass";

            compass.setStackDisplayName(name); // I18n.translateToLocal is deprecated?
            ItemBoundCompass.setPos(compass, compassPos);
            ItemBoundCompass.setColor(compass, color);

            NBTTagCompound tag = compass.getTagCompound();
            NBTTagCompound tag1 = tag.getCompoundTag("display");
            tag.setTag("display", tag1);
            tag1.setInteger("color", 0x80f030);

            // set the player to hold it or in their inventory
            PlayerHelper.setHeldItem(player, hand, compass);
        }
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event)
    {
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
