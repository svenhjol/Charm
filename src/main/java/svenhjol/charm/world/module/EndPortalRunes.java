package svenhjol.charm.world.module;

import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.charm.world.storage.RunePortalSavedData;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ClientHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Add colored runes in stronghold End Portal frames. Two portals with the same runes will be linked together.")
public class EndPortalRunes extends MesonModule {
    public static RunePortalBlock portal;
    public static RunePortalFrameBlock frame;

    @ObjectHolder("charm:rune_portal")
    public static TileEntityType<RunePortalTileEntity> tile;

    @Config(name = "Allow Eye of Ender removal", description = "If true, sneak-clicking with an empty hand or colored rune removes an eye of ender from its frame.")
    public static boolean allowEnderEyeRemoval = true;

    @OnlyIn(Dist.CLIENT)
    public static long clientTravelTicks;

    @OnlyIn(Dist.CLIENT)
    public static long clientUnlinkedTicks;

    @OnlyIn(Dist.CLIENT)
    public static long clientLinkedTicks;

    @Override
    @SuppressWarnings({"ConstantConditions"})
    public void init() {
        portal = new RunePortalBlock(this);
        frame = new RunePortalFrameBlock(this);

        // register TE
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "rune_portal");
        tile = TileEntityType.Builder.create(RunePortalTileEntity::new, portal).build(null);
        RegistryHandler.registerTile(tile, res);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(tile, RunePortalTileEntityRenderer::new);
    }

    @SubscribeEvent
    public void onRightClick(RightClickBlock event) {
        if (Charm.quarkCompat == null || !Charm.quarkCompat.hasColorRuneModule())
            return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();

        if (world.isRemote) return;
        ServerWorld serverWorld = (ServerWorld) world;

        boolean isVanilla = state.getBlock() == Blocks.END_PORTAL_FRAME;
        boolean isModded = state.getBlock() == frame;

        if (isVanilla || isModded) {
            BlockState changed = null;
            ItemStack toDrop = null;
            ItemStack held = player.getHeldItem(hand);

            if (Charm.quarkCompat.isRune(held) && !PlayerHelper.isCrouching(player)) {

                // if end portal frame, drop eye of ender
                if (isVanilla && state.get(EndPortalFrameBlock.EYE)) {
                    if (allowEnderEyeRemoval) {
                        toDrop = new ItemStack(Items.ENDER_EYE);
                    }

                    // set the end portal frame to an empty frame, facing the correct direction
                    changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .with(EndPortalFrameBlock.FACING, state.get(RunePortalFrameBlock.FACING));
                }

                // if a rune frame, drop the rune that is currently in it
                if (isModded) {
                    toDrop = Charm.quarkCompat.getRune(state.get(RunePortalFrameBlock.RUNE));
                }

                addRune(serverWorld, pos, held, player);
                activate(serverWorld, pos);

            } else if (PlayerHelper.isCrouching(player)) {

                if (isVanilla && allowEnderEyeRemoval && state.get(EndPortalFrameBlock.EYE)) {
                    toDrop = new ItemStack(Items.ENDER_EYE);
                }

                if (isModded) {
                    toDrop = Charm.quarkCompat.getRune(state.get(RunePortalFrameBlock.RUNE));
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

    public static void addRune(ServerWorld world, BlockPos pos, ItemStack rune, @Nullable PlayerEntity player) {
        BlockState state = world.getBlockState(pos);
        Direction facing;

        if (state.getBlock() == Blocks.END_PORTAL_FRAME) {
            facing = state.get(EndPortalFrameBlock.FACING);
        } else if (state.getBlock() == frame) {
            facing = state.get(RunePortalFrameBlock.FACING);
        } else {
            Meson.LOG.debug("Not a frame block: " + state);
            return;
        }

        ColorVariant color = Charm.quarkCompat.getRuneColor(rune);
        if (color == null) {
            Meson.LOG.debug("Failed to add rune");
            return;
        }

        BlockState changed = frame.getDefaultState()
            .with(RunePortalFrameBlock.FACING, facing)
            .with(RunePortalFrameBlock.RUNE, color);

        world.setBlockState(pos, changed, 2);

        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        if (player != null && !player.isCreative()) {
            rune.shrink(1);
        }
    }

    public static void activate(ServerWorld world, BlockPos pos) {
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
            for (int i : new int[]{2, 4, 3, 5}) {
                List<Integer> row = orderMap.get(i);
                Collections.sort(row);
                order.addAll(row);
            }

            int[] colors = order.stream().mapToInt(i -> i).toArray();
            data.portals.put(thisPortal, colors);
            data.markDirty();

            removeCachedPortal(world, thisPortal);

            if (findPortal(world, thisPortal) != null) {
                Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToAll(new ClientRunePortalAction(ClientRunePortalAction.LINKED, thisPortal));
            }

            BlockPos start = thisPortal.add(-3, 0, -3);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos p = start.add(j, 0, k);
                    world.setBlockState(p, portal.getDefaultState(), 2);
                    portal.setPortal(world, p, thisPortal, order);
                }
            }

            world.playSound(null, thisPortal, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public static void deactivate(ServerWorld world, BlockPos pos) {
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
                data.markDirty();

                removeCachedPortal(world, thisPortal[0]);

                if (!world.isRemote) {
                    Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToAll(new ClientRunePortalAction(ClientRunePortalAction.UNLINKED, thisPortal[0]));
                }
            }
        }
    }

    public static void removeCachedPortal(ServerWorld world, BlockPos portal) {
        RunePortalSavedData data = RunePortalSavedData.get(world);

        // find any cached portal linked to this
        BlockPos cachedPortal = data.links.get(portal);
        if (cachedPortal != null) {
            data.links.remove(cachedPortal);
            Meson.LOG.debug("EndPortalRunes: [CACHE] clearing cached link: " + cachedPortal);
        }

        data.links.clear();
        data.markDirty();
    }

    public static BlockPos findPortal(ServerWorld world, BlockPos thisPortal) {
        RunePortalSavedData data = RunePortalSavedData.get(world);
        BlockPos foundPortal = null;
        List<BlockPos> matching = new ArrayList<>();

        if (data.portals.containsKey(thisPortal)) {

            // check cache for link
            BlockPos linkedPortal = data.links.get(thisPortal);
            if (linkedPortal != null) {
                if (data.portals.containsKey(linkedPortal)) {
                    Meson.LOG.debug("EndPortalRunes: [CACHE] found portal: " + linkedPortal);
                    return linkedPortal;
                } else {
                    Meson.LOG.debug("EndPortalRunes: [CACHE] cleaning unlinked portal: " + linkedPortal);
                    data.links.remove(thisPortal);
                }
                data.markDirty();
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

        Meson.LOG.debug("EndPortalRunes: found matching portal, caching it: " + foundPortal);

        // cache portal both ways
        data.links.put(thisPortal, foundPortal);
        data.links.put(foundPortal, thisPortal);
        data.markDirty();

        return foundPortal;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            RunePortalSavedData.get((ServerWorld) event.getWorld());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectPortalLinked(BlockPos pos) {
        World world = ClientHelper.getClientWorld();
        PlayerEntity player = ClientHelper.getClientPlayer();
        long time = world.getGameTime();
        if (clientLinkedTicks == 0 || time - clientLinkedTicks > 20) {
            BlockPos current = pos.add(-3, 0, -3);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos p = current.add(j, 0, k);
                    for (int i = 0; i < 12; ++i) {
                        double d0 = world.rand.nextGaussian() * 0.1D;
                        double d1 = world.rand.nextGaussian() * 0.1D;
                        double d2 = world.rand.nextGaussian() * 0.1D;
                        double dx = (float) p.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        double dy = (float) p.getY() + 1.1f;
                        double dz = (float) p.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        world.addParticle(ParticleTypes.PORTAL, dx, dy, dz, d0, d1, d2);
                    }
                }
            }
            world.playSound(player, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.PLAYERS, 0.8F, 1.45F);
            clientLinkedTicks = time;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectPortalUnlinked(BlockPos pos) {
        World world = ClientHelper.getClientWorld();
        PlayerEntity player = ClientHelper.getClientPlayer();
        long time = world.getGameTime();
        if (clientUnlinkedTicks == 0 || time - clientUnlinkedTicks > 20) {
            BlockPos current = pos.add(-3, 0, -3);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos p = current.add(j, 0, k);
                    for (int i = 0; i < 16; ++i) {
                        double d0 = world.rand.nextGaussian() * 0.02D;
                        double d1 = world.rand.nextGaussian() * 0.02D;
                        double d2 = world.rand.nextGaussian() * 0.02D;
                        double dx = (float) p.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        double dy = (float) p.getY() + 0.5f;
                        double dz = (float) p.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        world.addParticle(ParticleTypes.LARGE_SMOKE, dx, dy, dz, d0, d1, d2);
                    }
                }
            }
            world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 0.75F, 0.7F);
            clientUnlinkedTicks = time;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectPortalTravelled(BlockPos pos) {
        World world = ClientHelper.getClientWorld();
        PlayerEntity player = ClientHelper.getClientPlayer();
        long time = world.getGameTime();
        if (clientTravelTicks == 0 || time - clientTravelTicks > 20) {
            BlockPos current = pos.add(-3, 0, -3);
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos p = current.add(j, 0, k);
                    for (int i = 0; i < 6; ++i) {
                        double d0 = world.rand.nextGaussian() * 0.1D;
                        double d1 = world.rand.nextGaussian() * 0.1D;
                        double d2 = world.rand.nextGaussian() * 0.1D;
                        double dx = (float) p.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        double dy = (float) p.getY() + 0.8f;
                        double dz = (float) p.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                        world.addParticle(ParticleTypes.CLOUD, dx, dy, dz, d0, d1, d2);
                    }
                }
            }
            world.playSound(player, pos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.75F, 1.0F);
            clientTravelTicks = time;
        }
    }
}
