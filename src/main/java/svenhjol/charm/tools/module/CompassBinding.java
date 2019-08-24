package svenhjol.charm.tools.module;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tools.item.BoundCompassItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TOOLS, hasSubscriptions = true)
public class CompassBinding extends MesonModule
{
    public static BoundCompassItem item;
    public static List<Item> bindableItems = new ArrayList<>();

    @Config(name = "Bind to Banners", description = "If true, compasses can bind to banners.")
    public static boolean useBanners = false;

    @Config(name = "Bind to Beacons", description = "If true, compasses can bind to beacons.")
    public static boolean useBeacons = true;

    @Override
    public void init()
    {
        item = new BoundCompassItem(this);
        bindableItems.add(Items.COMPASS);
        bindableItems.add(item);
    }

    @SubscribeEvent
    public void onCompassUse(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getEntityPlayer() != null
            && event.getEntityPlayer().isSneaking()
            && bindableItems.contains(event.getEntityPlayer().getHeldItem(event.getHand()).getItem())
        ) {
            PlayerEntity player = event.getEntityPlayer();
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = world.getBlockState(pos);
            Hand hand = event.getHand();

            boolean validCompass = false;
            int color = 0;
            String name = "";
            ItemStack compass = new ItemStack(item);
            TileEntity tile = world.getTileEntity(pos);

            // handle banners
            if (useBanners && tile instanceof BannerTileEntity) {
                BannerTileEntity banner = (BannerTileEntity) tile;
                DyeColor c = banner.getBaseColor(() -> state);
                color = c.getMapColor().colorValue;

                if (banner.hasCustomName()) {
                    name = banner.getDisplayName().toString();
                }
                validCompass = true;
            }

            // handle beacons
            if (useBeacons && tile instanceof BeaconTileEntity) {
                BeaconTileEntity beacon = (BeaconTileEntity)tile;
                if (beacon.getLevels() < 1) return; // better way to detect active beacons?
                name = Objects.requireNonNull(beacon.getDisplayName()).getString();
                validCompass = true;
            }

            if (!validCompass) return;

            // set up the new compass details
            BlockPos compassPos = new BlockPos(pos);
            name = (name.length()) > 0 ? name : "Bound Compass";

            compass.setDisplayName(new StringTextComponent(name));
            BoundCompassItem.setPos(compass, compassPos);
            BoundCompassItem.setDim(compass, world.getDimension().getType().getId());
            BoundCompassItem.setColor(compass, color);

            // set the player to hold it or in their inventory
            PlayerHelper.setHeldItem(player, hand, compass);
        }
    }
}
