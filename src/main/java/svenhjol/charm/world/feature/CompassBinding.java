package svenhjol.charm.world.feature;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.world.item.BoundCompassItem;
import svenhjol.meson.Feature;
import svenhjol.meson.helpers.PlayerHelper;

import java.util.ArrayList;
import java.util.List;

public class CompassBinding extends Feature
{
    public static BoundCompassItem item;
    public static List<Item> bindableItems = new ArrayList<>();
    public static ForgeConfigSpec.BooleanValue useBanners;

    @Override
    public void configure()
    {
        super.configure();

        useBanners = builder
            .comment("If true, compasses can bind to banners.")
            .define("Bind to Banners", true);
    }

    @Override
    public void init()
    {
        super.init();
        item = new BoundCompassItem();
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
            if (useBanners.get() && tile instanceof BannerTileEntity) {
                BannerTileEntity banner = (BannerTileEntity) tile;
                DyeColor c = banner.getBaseColor(() -> state);
                color = c.getMapColor().colorValue;

                if (banner.hasCustomName()) {
                    name = banner.getDisplayName().toString();
                }
                validCompass = true;
            }

            if (!validCompass) return;

            // set up the new compass details
            BlockPos compassPos = new BlockPos(pos);
            name = (name.length()) > 0 ? name : "Bound Compass";

            compass.setDisplayName(new StringTextComponent(name)); // I18n.translateToLocal is deprecated?
            BoundCompassItem.setPos(compass, compassPos);
            BoundCompassItem.setDim(compass, world.getDimension().getType().getId());
            BoundCompassItem.setColor(compass, color);

            // set the player to hold it or in their inventory
            PlayerHelper.setHeldItem(player, hand, compass);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
