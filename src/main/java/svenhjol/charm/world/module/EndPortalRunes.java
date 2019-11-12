package svenhjol.charm.world.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.block.RunePortalFrameBlock;
import svenhjol.charm.world.compat.QuarkRunes;
import svenhjol.charm.world.storage.RunePortalSavedData;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Add colored runes in stronghold End Portal frames. Two portals with the same runes will be linked together.")
public class EndPortalRunes extends MesonModule
{
    public static RunePortalFrameBlock block;

    private static QuarkRunes quarkRunes;

    @Override
    public void init()
    {
        block = new RunePortalFrameBlock(this);

        try {
            if (ForgeHelper.isModLoaded("quark")) {
                quarkRunes = QuarkRunes.class.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading QuarkRunes");
        }
    }

    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && quarkRunes != null;
    }

    @SubscribeEvent
    public void onRightClick(RightClickBlock event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();

        if (world.isRemote) return;

        boolean isVanilla = state.getBlock() == Blocks.END_PORTAL_FRAME;
        boolean isModded = state.getBlock() == block;

        if (isVanilla || isModded) {
            BlockState changed = null;
            ItemStack toDrop = null;
            ItemStack held = player.getHeldItem(hand);

            if (quarkRunes.isRune(held) && !player.isSneaking()) {
                ColorVariant heldRuneColor = quarkRunes.getColor(held);

                // if end portal frame, drop eye of ender
                if (isVanilla && state.get(EndPortalFrameBlock.EYE)) {
                    toDrop = new ItemStack(Items.ENDER_EYE);

                    // set the end portal frame to an empty frame, facing the correct direction
                    changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .with(EndPortalFrameBlock.FACING, state.get(RunePortalFrameBlock.FACING));

                    world.setBlockState(pos, changed, 2);
                }

                // if a rune frame, drop the rune that is currently in it
                if (isModded) {
                    toDrop = quarkRunes.getRune(state.get(RunePortalFrameBlock.RUNE));
                }

                addRune(world, pos, held);

                // TODO activate portal

            } else if (player.isSneaking()) {

                if (isVanilla && state.get(EndPortalFrameBlock.EYE)) {
                    toDrop = new ItemStack(Items.ENDER_EYE);
                }

                if (isModded) {
                    toDrop = quarkRunes.getRune(state.get(RunePortalFrameBlock.RUNE));
                }

                if (toDrop != null) {
                    // set the end portal frame to an empty frame, facing the correct direction
                    changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .with(EndPortalFrameBlock.FACING, state.get(RunePortalFrameBlock.FACING));
                }

                // TODO deactivate portal

                if (toDrop != null) {
                    event.setCanceled(true); // don't allow clickthrough
                }
            }

            // if the state needs to be changed, do it now
            if (changed != null) {
                world.setBlockState(pos, changed, 2);
            }

            // if anything needs to be dropped, do it now
            if (toDrop != null) {
                PlayerHelper.addOrDropStack(player, toDrop);
            }
        }
    }

    public static void addRune(World world, BlockPos pos, ItemStack rune)
    {
        BlockState state = world.getBlockState(pos);
        Direction facing = null;

        if (state.getBlock() == Blocks.END_PORTAL_FRAME) {
            facing = state.get(EndPortalFrameBlock.FACING);
        } else if (state.getBlock() == block) {
            facing = state.get(RunePortalFrameBlock.FACING);
        } else {
            Meson.debug("Not a frame block!", state);
            return;
        }

        ColorVariant color = quarkRunes.getColor(rune);
        if (color == null) {
            Meson.debug("Failed to add rune");
            return;
        }

        BlockState changed = block.getDefaultState()
            .with(RunePortalFrameBlock.FACING, facing)
            .with(RunePortalFrameBlock.RUNE, color);

        world.setBlockState(pos, changed, 2);

        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        rune.shrink(1);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ServerWorld) {
            RunePortalSavedData.get((ServerWorld)event.getWorld());
        }
    }
}
