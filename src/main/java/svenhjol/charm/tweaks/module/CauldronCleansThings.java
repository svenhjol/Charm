package svenhjol.charm.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.helper.CauldronHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Filled cauldrons can be used to clean certain blocks.")
public class CauldronCleansThings extends MesonModule {

    @Config(name = "Clean sticky pistons", description = "Sticky pistons become normal pistons.")
    public static boolean stickyPistons = true;

    @Config(name = "Clean colored wool", description = "Colored wool becomes white wool.")
    public static boolean wool = true;

    @Config(name = "Clean colored carpet", description = "Colored carpet becomes white carpet.")
    public static boolean carpet = true;

    @Config(name = "Clean quilted wool", description = "If Quark's Quilted Wool module is enabled, colored quilted wool becomes white quilted wool.")
    public static boolean quiltedWool = true;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCauldronUse(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.CAULDRON
            && event.getPlayer() != null
            && event.getPlayer().getHeldItem(event.getHand()) != ItemStack.EMPTY
        ) {
            final PlayerEntity player = event.getPlayer();
            final World world = player.world;
            final BlockPos pos = event.getPos();
            final ItemStack held = player.getHeldItem(event.getHand());
            final BlockState state = event.getWorld().getBlockState(event.getPos());
            final ResourceLocation res = held.getItem().getRegistryName();
            final Hand hand = event.getHand();

            ItemStack replace = null;

            if (!CauldronHelper.isFull(state))
                return;

            if (res == null)
                return;

            final String path = res.getPath();
            final String name = res.getNamespace();

            if (stickyPistons && name.equals("minecraft") && path.equals("sticky_piston")) {
                replace = new ItemStack(Blocks.PISTON);
            } else if (wool && name.equals("minecraft") && path.contains("_wool") && !path.equals("white_wool")) {
                replace = new ItemStack(Blocks.WHITE_WOOL);
            } else if (carpet && name.equals("minecraft") && path.contains("_carpet") && !path.equals("white_carpet")) {
                replace = new ItemStack(Blocks.WHITE_CARPET);
            } else if (quiltedWool && Charm.quarkCompat != null && name.equals("quark") && path.contains("_quilted_wool")) {
                replace = Charm.quarkCompat.getQuiltedWool(ColorVariant.WHITE);
            }

            if (replace != null) {
                player.setHeldItem(hand, replace);
                CauldronHelper.clearCauldron(world, pos, state);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                event.setCanceled(true);
            }
        }
    }
}
