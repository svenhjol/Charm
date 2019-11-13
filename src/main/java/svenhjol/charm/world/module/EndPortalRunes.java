package svenhjol.charm.world.module;

import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.block.RunePortalBlock;
import svenhjol.charm.world.block.RunePortalFrameBlock;
import svenhjol.charm.world.client.renderer.RunePortalTileEntityRenderer;
import svenhjol.charm.world.compat.QuarkRunes;
import svenhjol.charm.world.storage.RunePortalSavedData;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Add colored runes in stronghold End Portal frames. Two portals with the same runes will be linked together.")
public class EndPortalRunes extends MesonModule
{
    public static RunePortalBlock portal;
    public static RunePortalFrameBlock frame;

    @ObjectHolder("charm:rune_portal")
    public static TileEntityType<RunePortalTileEntity> tile;

    private static QuarkRunes quarkRunes;

    @Override
    public void init()
    {
        portal = new RunePortalBlock(this);
        frame = new RunePortalFrameBlock(this);

        // register TE
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "rune_portal");
        tile = TileEntityType.Builder.create(RunePortalTileEntity::new, portal).build(null);
        RegistryHandler.registerTile(tile, res);

        try {
            if (ForgeHelper.isModLoaded("quark")) {
                quarkRunes = QuarkRunes.class.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading QuarkRunes");
        }
    }

    @Override
    public void setupClient(FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(RunePortalTileEntity.class, new RunePortalTileEntityRenderer());
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
        ServerWorld serverWorld = (ServerWorld)world;

        boolean isVanilla = state.getBlock() == Blocks.END_PORTAL_FRAME;
        boolean isModded = state.getBlock() == frame;

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

                    serverWorld.setBlockState(pos, changed, 2);
                }

                // if a rune frame, drop the rune that is currently in it
                if (isModded) {
                    toDrop = quarkRunes.getRune(state.get(RunePortalFrameBlock.RUNE));
                }

                addRune(serverWorld, pos, held);
                activate(serverWorld, pos);

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

                deactivate(serverWorld, pos);

                if (toDrop != null) {
                    event.setCanceled(true); // don't allow clickthrough
                }
            }

            // if the state needs to be changed, do it now
            if (changed != null) {
                serverWorld.setBlockState(pos, changed, 2);
            }

            // if anything needs to be dropped, do it now
            if (toDrop != null) {
                PlayerHelper.addOrDropStack(player, toDrop);
            }
        }
    }

    public static void addRune(ServerWorld world, BlockPos pos, ItemStack rune)
    {
        BlockState state = world.getBlockState(pos);
        Direction facing = null;

        if (state.getBlock() == Blocks.END_PORTAL_FRAME) {
            facing = state.get(EndPortalFrameBlock.FACING);
        } else if (state.getBlock() == frame) {
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

        BlockState changed = frame.getDefaultState()
            .with(RunePortalFrameBlock.FACING, facing)
            .with(RunePortalFrameBlock.RUNE, color);

        world.setBlockState(pos, changed, 2);

        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        rune.shrink(1);
    }

    public static void activate(ServerWorld world, BlockPos pos)
    {
        Map<Integer, List<Integer>> orderMap = new HashMap<>();
        BlockPos thisPortal;
        BlockPattern.PatternHelper pattern = RunePortalFrameBlock.getOrCreatePortalShape().match(world, pos);

        if (pattern != null) {
            BlockState state;

            BlockPos patternStart = pattern.getFrontTopLeft().add(-5, 0, -5);
            for (int a = 0; a < 6; a++) {
                for (int b = 0; b < 6; b++) {
                    state = world.getBlockState(patternStart.add(a, 0, b));
                    if (state.getBlock() == frame) {
                        int face = state.get(RunePortalFrameBlock.FACING).getIndex();
                        if (!orderMap.containsKey(face)) orderMap.put(face, new ArrayList<>());
                        orderMap.get(face).add(state.get(RunePortalFrameBlock.RUNE).ordinal());
                    }
                }
            }

            thisPortal = pattern.getFrontTopLeft();
            RunePortalSavedData data = RunePortalSavedData.get(world);
            List<Integer> order = new ArrayList<>();
            for (int i : new int[] {2, 4, 3, 5}) {
                List<Integer> row = orderMap.get(i);
                Collections.sort(row);
                order.addAll(row);
            }

            data.portals.put(thisPortal, order.stream().mapToInt(i -> i).toArray());

            // TODO remove cached portal


            if (findPortal(world, thisPortal) != null) {
                // TODO network message
            }

            BlockPos start = thisPortal.add(-3, 0, -3);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos p = start.add(j, 0, k);
                    world.setBlockState(p, portal.getDefaultState(), 2);
                    portal.setPortal(world, p, thisPortal);
                }
            }
        }
    }

    public static void deactivate(ServerWorld world, BlockPos pos)
    {
        final BlockPos[] thisPortal = {null};
        BlockPattern.PatternHelper pattern = RunePortalFrameBlock.getOrCreatePortalShape().match(world, pos);

        if (pattern == null) {
            BlockPos pos1 = pos.add(-3, 0, -3);
            BlockPos pos2 = pos.add(3, 0, 3);

            Stream<BlockPos> inRange = BlockPos.getAllInBox(pos1, pos2);

            inRange.forEach(p -> {
                Block block = world.getBlockState(p).getBlock();
                if (block instanceof EndPortalBlock) { // BlockRunePortal inherits
                    if (thisPortal[0] == null) {
                        thisPortal[0] = portal.getPortal(world, p);
                    }
                    world.setBlockState(p, Blocks.AIR.getDefaultState());
                }
            });

            if (thisPortal[0] != null) {
                RunePortalSavedData data = RunePortalSavedData.get(world);
                data.portals.remove(thisPortal[0]);

                // TODO cache

                if (!world.isRemote) {
                    // TODO message
                }
            }
        }
    }

    public static BlockPos findPortal(ServerWorld world, BlockPos thisPortal)
    {
        RunePortalSavedData data = RunePortalSavedData.get(world);
        BlockPos foundPortal = null;
        List<BlockPos> matching = new ArrayList<>();

        if (data.portals.containsKey(thisPortal)) {

            // check cache for link
            BlockPos linkedPortal = data.links.get(thisPortal);
            if (linkedPortal != null) {
                if (data.portals.containsKey(linkedPortal)) {
                    Meson.debug("EndPortalRunes: [CACHE] found portal", linkedPortal);
                    return linkedPortal;
                } else {
                    Meson.debug("EndPortalRunes: [CACHE] cleaning unlinked portal", linkedPortal);
                    data.links.remove(thisPortal);
                }
            }
            List<Integer> o1 = Arrays.stream(data.portals.get(thisPortal)).boxed().collect(Collectors.toList());

            for (BlockPos portalPos : data.portals.keySet()) {
                if (portalPos.toLong() == thisPortal.toLong()) continue;

                List<Integer> o2 = Arrays.stream(data.portals.get(portalPos)).boxed().collect(Collectors.toList());
                boolean matched;
                int w = 0;

                do { // shift array until found
                    Collections.rotate(o2, 1);
                    matched = o1.equals(o2);
                } while (!matched && ++w < 12);

                if (matched) {
                    matching.add(portalPos);
                }
            }
        }

        if (matching.isEmpty()) return null;

        // get closest
        double dist = 0;
        for (BlockPos matchedPortal : matching) {
            double d = WorldHelper.getDistanceSq(thisPortal, matchedPortal);
            if (dist == 0 || d < dist) {
                foundPortal = matchedPortal;
                dist = d;
            }
        }

        Meson.debug("EndPortalRunes: found matching portal, caching it", foundPortal);

        // cache portal both ways
        data.links.put(thisPortal, foundPortal);
        data.links.put(foundPortal, thisPortal);

        return foundPortal;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (event.getWorld() instanceof ServerWorld) {
            RunePortalSavedData.get((ServerWorld)event.getWorld());
        }
    }
}
