package svenhjol.charm.world.feature;

import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.world.block.BlockRunePortal;
import svenhjol.charm.world.block.BlockRunePortalFrame;
import svenhjol.charm.world.compat.QuarkColoredRunes;
import svenhjol.charm.world.message.MessagePortalInteract;
import svenhjol.charm.world.storage.RunePortalSavedData;
import svenhjol.charm.world.tile.TileRunePortalFrame;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonBlock;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.helper.WorldHelper;

import java.util.*;

public class EndPortalRunes extends Feature
{
    public static BlockRunePortalFrame frame;
    public static BlockRunePortal portal;
    public static QuarkColoredRunes quarkRunes;
    public static boolean allowEnderEyeRemoval;

    @Override
    public void setupConfig()
    {
        allowEnderEyeRemoval = true;
    }

    @Override
    public String[] getRequiredMods()
    {
        return new String[] { "quark" };
    }

    @Override
    public boolean checkSelf()
    {
        try {
            quarkRunes = QuarkColoredRunes.class.getConstructor().newInstance();
        } catch (Exception e) {
            Meson.log("Error loading QuarkColoredRunes compat class");
            return false;
        }

        if (!quarkRunes.hasColorRuneFeature()) {
            Meson.log("Quark ColorRunes feature not enabled");
            return false;
        }

        return true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        portal = new BlockRunePortal();
        frame = new BlockRunePortalFrame();

        GameRegistry.registerTileEntity(portal.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":rune_portal"));
        GameRegistry.registerTileEntity(frame.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":rune_portal_frame"));

        NetworkHandler.register(MessagePortalInteract.class, Side.CLIENT);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = event.getWorld();
        RunePortalSavedData.get(world);
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        IBlockState current = event.getWorld().getBlockState(event.getPos()).getActualState(event.getWorld(), event.getPos());
        boolean isVanillaFrame = current.getBlock() == Blocks.END_PORTAL_FRAME;
        boolean isModdedFrame = current.getBlock() == frame;

        if (isVanillaFrame || isModdedFrame) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();
            ItemStack held = event.getEntityPlayer().getHeldItem(event.getHand());

            boolean holdingRune = EndPortalRunes.quarkRunes.isRune(held);
            boolean holdingEnderEye = held.getItem() == Items.ENDER_EYE;

            if (holdingRune && !player.isSneaking()) {

                if (isVanillaFrame && current.getValue(BlockEndPortalFrame.EYE) && allowEnderEyeRemoval) {
                    // if there's an eye then pop it
                    PlayerHelper.addOrDropStack(player, new ItemStack(Items.ENDER_EYE, 1));
                    EnumFacing frameFacing = current.getValue(BlockRunePortalFrame.FACING);
                    world.setBlockState(pos, Blocks.END_PORTAL_FRAME.getDefaultState()
                        .withProperty(BlockEndPortalFrame.FACING, frameFacing));
                    deactivate(world, pos);
                }

                if (isModdedFrame) {
                    // if there's a rune then pop it
                    MesonBlock.ColorVariant frameColor = current.getValue(BlockRunePortalFrame.VARIANT);
                    PlayerHelper.addOrDropStack(player, EndPortalRunes.quarkRunes.getRuneFromMeta(frameColor.ordinal()));
                }

                // add the rune to the portal frame
                addRune(world, pos, held);
                activate(world, pos);

            } else if (!holdingEnderEye && player.isSneaking()) {

                IBlockState changed = null;

                if (isVanillaFrame && !world.isRemote && current.getValue(BlockEndPortalFrame.EYE) && allowEnderEyeRemoval) {
                    // if there 's an eye then pop it
                    PlayerHelper.addOrDropStack(player, new ItemStack(Items.ENDER_EYE, 1));
                    changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .withProperty(BlockEndPortalFrame.FACING, current.getValue(BlockEndPortalFrame.FACING));
                }

                if (isModdedFrame) {
                    // if there's a rune then pop it
                    MesonBlock.ColorVariant frameColor = current.getValue(BlockRunePortalFrame.VARIANT);
                    PlayerHelper.addOrDropStack(player, EndPortalRunes.quarkRunes.getRuneFromMeta(frameColor.ordinal()));

                    EnumFacing frameFacing = current.getValue(BlockRunePortalFrame.FACING);
                    changed = Blocks.END_PORTAL_FRAME.getDefaultState()
                        .withProperty(BlockEndPortalFrame.FACING, frameFacing);
                }

                if (changed != null) {
                    world.setBlockState(pos, changed, 3);
                }

                deactivate(world, pos);
                event.setCanceled(true);
            }

        }
    }

    public static void addRune(World world, BlockPos pos, ItemStack rune)
    {
        TileEntity tile;
        IBlockState current = world.getBlockState(pos);
        EnumFacing currentFacing = current.getValue(BlockRunePortalFrame.FACING);

        // get the facing from the TE if possible
        tile = world.getTileEntity(pos);
        if (tile instanceof TileRunePortalFrame) {
            currentFacing = ((TileRunePortalFrame)tile).getFacing();
        }

        // add a rune to the frame
        IBlockState changed = frame.getDefaultState()
            .withProperty(BlockRunePortalFrame.VARIANT, quarkRunes.getRuneColor(rune))
            .withProperty(BlockRunePortalFrame.FACING, currentFacing);

        world.setBlockState(pos, changed, 2);

        // set facing
        tile = world.getTileEntity(pos);
        if (tile instanceof TileRunePortalFrame) {
            ((TileRunePortalFrame)tile).setFacing(currentFacing);
        }
        world.setBlockState(pos, changed, 2);

        if (world.isRemote) {
            SoundHelper.playSoundAtPos(world, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 1.0f);
        }

        rune.shrink(1);
    }

    public static void activate(World world, BlockPos pos)
    {
        Map<Integer, List<String>> orderMap = new HashMap<>();
        BlockPos thisPortal;
        BlockPattern.PatternHelper pattern = BlockRunePortalFrame.getOrCreatePortalShape().match(world, pos);

        if (pattern != null) {
            IBlockState state;
            BlockPos patternStart = pattern.getFrontTopLeft().add(-5, 0, -5);

            for (int a = 0; a < 6; a++) {
                for (int b = 0; b < 6; b++) {
                    state = world.getBlockState(patternStart.add(a, 0, b));
                    if (state.getBlock() == frame) {
                        int face = state.getValue(BlockRunePortalFrame.FACING).getIndex();
                        if (!orderMap.containsKey(face)) orderMap.put(face, new ArrayList<>());
                        orderMap.get(face).add(Integer.toString(frame.getMetaFromState(state)));
                    }
                }
            }

            thisPortal = pattern.getFrontTopLeft();

            // write the portal to data
            RunePortalSavedData data = RunePortalSavedData.get(world);

            if (data != null) {
                List<String> order = new ArrayList<>();

                for (int i : new int[] {2, 4, 3, 5}) {
                    List<String> row = orderMap.get(i);
//                    if (Integer.valueOf(row.get(2)) < Integer.valueOf(row.get(0))) {
//                        Collections.reverse(row);
//                    }
                    Collections.sort(row);
                    order.addAll(row);
                }

                data.portals.put(thisPortal, String.join(" ", order));
                removeCachedPortal(world, thisPortal);
                data.markDirty();
            }

            if (findPortal(world, thisPortal) != null && !world.isRemote) {
                NetworkHandler.INSTANCE.sendToAll(new MessagePortalInteract(thisPortal, 1));
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

    public static void deactivate(World world, BlockPos pos)
    {
        BlockPos thisPortal = null;
        BlockPattern.PatternHelper pattern = BlockRunePortalFrame.getOrCreatePortalShape().match(world, pos);

        if (pattern == null) {
            BlockPos pos1 = pos.add(-3, 0, -3);
            BlockPos pos2 = pos.add(3, 0, 3);

            Iterable<BlockPos> box = BlockPos.getAllInBox(pos1, pos2);
            for (BlockPos inBox : box) {
                if (world.getBlockState(inBox).getBlock() instanceof BlockEndPortal) { // BlockRunePortal inherits
                    if (thisPortal == null) {
                        thisPortal = portal.getPortal(world, inBox);
                    }
                    world.setBlockToAir(inBox);
                }
            }

            if (thisPortal != null) {
                RunePortalSavedData data = RunePortalSavedData.get(world);

                if (data != null) {
                    data.portals.remove(thisPortal);
                    removeCachedPortal(world, thisPortal);
                    data.markDirty();
                }

                if (!world.isRemote) {
                    NetworkHandler.INSTANCE.sendToAll(new MessagePortalInteract(thisPortal, 0));
                }
            }
        }
    }

    public static BlockPos findPortal(World world, BlockPos thisPortal)
    {
        RunePortalSavedData data = RunePortalSavedData.get(world);
        BlockPos foundPortal = null;
        List<BlockPos> matching = new ArrayList<>();

        if (data != null && data.portals.containsKey(thisPortal)) {

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
//            {2=[1, 1, 14], 3=[1, 1, 1], 4=[9, 1, 1], 5=[1, 1, 1]}
            //{2=[1, 1, 1], 3=[9, 1, 1], 4=[14, 1, 1], 5=[1, 1, 1]}

            String order = data.portals.get(thisPortal);

            for (BlockPos portalPos : data.portals.keySet()) {
                if (portalPos.toLong() == thisPortal.toLong()) continue;

                String[] portalOrder = data.portals.get(portalPos).split(" ");
                String[] newOrder = new String[portalOrder.length];
                boolean matched;
                int w = 0;

                // shift
                do {
                    Collections.rotate(Arrays.asList(portalOrder), 1);
                    String n = String.join(" ", portalOrder);
                    matched = order.equals(n);
//                    Meson.log("trying " + order + " against " + n);
                } while (!matched && ++w < 12);

                if (matched) matching.add(portalPos);
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

    public static void removeCachedPortal(World world, BlockPos portal)
    {
        RunePortalSavedData data = RunePortalSavedData.get(world);

        if (data != null) {

            // find any cached portal linked to this
            BlockPos cachedPortal = data.links.get(portal);
            if (cachedPortal != null) {
                data.links.remove(cachedPortal);
            }

            data.links.clear();
            data.markDirty();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void effectPortalLinked(BlockPos pos)
    {
        World world = Minecraft.getMinecraft().world;
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
                    world.spawnParticle(EnumParticleTypes.PORTAL, dx, dy, dz, d0, d1, d2);
                }
            }
        }
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.BLOCK_PORTAL_TRIGGER,0.8F, 1.45F);
    }

    @SideOnly(Side.CLIENT)
    public static void effectPortalUnlinked(BlockPos pos)
    {
        World world = Minecraft.getMinecraft().world;
        BlockPos current = pos.add(-3, 0, -3);
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                BlockPos p = current.add(j, 0, k);
                for (int i = 0; i < 16; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    double dx = (float)p.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                    double dy = (float)p.getY() + 0.5f;
                    double dz = (float)p.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, dx, dy, dz, d0, d1, d2);
                }
            }
        }

        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,0.75F, 0.7F);
    }

    public static void effectPortalTravelled(BlockPos pos)
    {
        World world = Minecraft.getMinecraft().world;
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
                    world.spawnParticle(EnumParticleTypes.CLOUD, dx, dy, dz, d0, d1, d2);
                }
            }
        }

        SoundHelper.playSoundAtPos(world, pos, SoundEvents.BLOCK_PORTAL_TRAVEL,0.75F, 1.0F);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
