package svenhjol.charm.world.feature;

import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.world.block.BlockEndPortalFrameRunes;
import svenhjol.charm.world.compat.QuarkColoredRunes;
import svenhjol.charm.world.tile.TileEndPortalRunes;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonBlock;
import svenhjol.meson.helper.PlayerHelper;

public class EndPortalRunes extends Feature
{
    public static BlockEndPortalFrameRunes portalFrame;
    public static QuarkColoredRunes quarkRunes;

    @Override
    public String[] getRequiredMods()
    {
        return new String[] { "quark" };
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        try {
            quarkRunes = QuarkColoredRunes.class.getConstructor().newInstance();
        } catch (Exception e) {
            Meson.runtimeException("Error loading QuarkColoredRunes");
        }

        portalFrame = new BlockEndPortalFrameRunes();
        GameRegistry.registerTileEntity(portalFrame.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":end_portal_frame_runes"));
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        IBlockState current = event.getWorld().getBlockState(event.getPos());

        boolean isEndPortalFrame = current.getBlock() == Blocks.END_PORTAL_FRAME;
        boolean isEndPortalRune = current.getBlock() == portalFrame;

        if (isEndPortalFrame || isEndPortalRune) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack held = event.getEntityPlayer().getHeldItem(event.getHand());

            boolean holdingRune = EndPortalRunes.quarkRunes.isRune(held);

            if (holdingRune) {

//                if (isEndPortalFrame && current.getValue(BlockEndPortalFrame.EYE)) {
                    // if there's an eye then pop it
//                    PlayerHelper.addOrDropStack(player, new ItemStack(Items.ENDER_EYE));
                if (isEndPortalRune) {
                    // if there's a rune then pop it
                    MesonBlock.ColorVariant frameColor = current.getValue(BlockEndPortalFrameRunes.COLOR);
                    PlayerHelper.addOrDropStack(player, EndPortalRunes.quarkRunes.getRuneFromMeta(frameColor.ordinal()));
                }

                // add the rune to the portal frame
                addRune(world, pos, held);

            } else if (player.isSneaking()) {

                if (isEndPortalRune) {
                    // if there's a rune then pop it
                    MesonBlock.ColorVariant frameColor = current.getValue(BlockEndPortalFrameRunes.COLOR);
                    PlayerHelper.addOrDropStack(player, EndPortalRunes.quarkRunes.getRuneFromMeta(frameColor.ordinal()));

                    EnumFacing frameFacing = current.getValue(BlockEndPortalFrameRunes.FACING);
                    IBlockState changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .withProperty(BlockEndPortalFrame.FACING, frameFacing);

                    world.setBlockState(pos, changed, 2);
                }
            }
        }
    }

    public static void addRune(World world, BlockPos pos, ItemStack rune)
    {
        IBlockState current = world.getBlockState(pos);
        EnumFacing currentFacing = current.getValue(BlockEndPortalFrameRunes.FACING);

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEndPortalRunes) {
            TileEndPortalRunes te = (TileEndPortalRunes)tile;
            currentFacing = te.getFacing();
        }

        // add a rune to the frame
        IBlockState changed = portalFrame.getDefaultState()
            .withProperty(BlockEndPortalFrameRunes.COLOR, quarkRunes.getRuneColor(rune))
            .withProperty(BlockEndPortalFrameRunes.FACING, currentFacing);

        world.setBlockState(pos, changed, 3);

        // set facing
        tile = world.getTileEntity(pos);
        if (tile instanceof TileEndPortalRunes) {
            TileEndPortalRunes te = (TileEndPortalRunes)tile;
            te.setFacing(currentFacing);
        }
        rune.shrink(1);
    }

    public static ItemStack removeRune(World world, BlockPos pos)
    {
        IBlockState current = world.getBlockState(pos);

        if (current.getBlock() == portalFrame) {
            MesonBlock.ColorVariant frameColor = current.getValue(BlockEndPortalFrameRunes.COLOR);
            EnumFacing frameFacing = current.getValue(BlockEndPortalFrameRunes.FACING);
            ItemStack rune = EndPortalRunes.quarkRunes.getRuneFromMeta(frameColor.ordinal());

            IBlockState changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                .withProperty(BlockEndPortalFrame.FACING, frameFacing);

            world.setBlockState(pos, changed, 2);
            return rune;
        }

        return null;
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
