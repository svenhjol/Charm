package svenhjol.charm.crafting.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.crafting.block.BlockCrate;
import svenhjol.charm.crafting.client.RenderTileCrate;
import svenhjol.charm.crafting.tile.TileCrate;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.LootHelper;

import java.util.*;

public class Crate extends Feature
{
    public static BlockCrate crate;
    public static BlockCrate crateSealed;
    public static boolean showCrateNames;
    public static int xpCost;

    public enum RARITY {
        COMMON,
        UNCOMMON,
        VALUABLE,
        RARE
    }

    public static Map<RARITY, List<CrateType>> types = new HashMap<>();

    public class CrateType
    {
        public String name;
        public String id;
        public ResourceLocation pool;

        @SuppressWarnings("deprecation")
        public CrateType(String id, ResourceLocation pool)
        {
            this.id = id;
            this.name = I18n.translateToLocal(id) + " " + I18n.translateToLocal("crate"); // deprecated, but what's the alternative?
            this.pool = pool;
        }
    }

    public static CrateType getRandomCrateType(RARITY r)
    {
        Random rand = new Random();
        List<CrateType> t = types.get(r);
        return t.get(rand.nextInt(t.size()));
    }

    @Override
    public String getDescription()
    {
        return "Offers a smaller storage solution with the benefit of being transportable.  Name your crate on an anvil to change the displayed label.\n" +
                "You can also seal the crate by combining it with an iron ingot - the only way to get things out is to break it.";
    }

    @Override
    public void setupConfig()
    {
        showCrateNames = propBoolean(
                "Show crate name",
                "Enables the display of the crate's name when you look at the crate.",
                true
        );
        xpCost = propInt(
                "XP cost",
                "Amount of XP (levels) required to use a name tag to seal a crate.",
                0
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // register the crate and sealed crate blocks
        crate = new BlockCrate(BlockCrate.Type.CRATE);
        crateSealed = new BlockCrate(BlockCrate.Type.CRATE_SEALED);
        GameRegistry.registerTileEntity(crate.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":crate"));

        //  get all loot tables for each rarity type
        Map<RARITY, List<ResourceLocation>> map = new HashMap<RARITY, List<ResourceLocation>>() {{
            put(RARITY.COMMON, LootHelper.getLootTables(LootHelper.RARITY.COMMON, LootHelper.TYPE.MISC));
            put(RARITY.UNCOMMON, LootHelper.getLootTables(LootHelper.RARITY.UNCOMMON, LootHelper.TYPE.MISC));
            put(RARITY.VALUABLE, LootHelper.getLootTables(LootHelper.RARITY.VALUABLE, LootHelper.TYPE.MISC));
            put(RARITY.RARE, LootHelper.getLootTables(LootHelper.RARITY.RARE, LootHelper.TYPE.MISC));
        }};

        // add crate types based on loot tables
        for (RARITY rarity : map.keySet()) {
            List<ResourceLocation> tables = map.get(rarity);
            List<CrateType> crates = new ArrayList<>();

            for (ResourceLocation res : tables) {
                String[] a = res.getPath().split("/");
                crates.add(new CrateType(a.length > 1 ? a[1] : a[0], res));
            }

            types.put(rarity, crates);
        }

        // add a couple of crate-specific treasures
        types.get(RARITY.VALUABLE).add(new CrateType("dangerous", CharmLootTables.TREASURE_DANGEROUS));
        types.get(RARITY.RARE).add(new CrateType("explosive", CharmLootTables.TREASURE_EXPLOSIVE));
    }

    public static ItemStack getSealedCrateItem(ItemStack in, String name)
    {
        ItemStack out = new ItemStack(Crate.crateSealed);

        if (name == null || name.isEmpty()) {
            name = in.getDisplayName();
        }

        NBTTagCompound inTag = in.getTagCompound();
        NBTTagCompound outTag;

        if (inTag != null && !inTag.isEmpty() && !inTag.getCompoundTag("BlockEntityTag").isEmpty()) {
            outTag = inTag.copy();
        } else {
            outTag = out.getOrCreateSubCompound("BlockEntityTag");
        }

        outTag.getCompoundTag("BlockEntityTag").setString("name", name);
        out.setTagCompound(outTag);
        out.setStackDisplayName(name);

        return out;
    }

    public static void generateCrate(World world, BlockPos pos, CrateType crateType, boolean sealed)
    {
        IBlockState state = sealed ? crateSealed.getDefaultState() : crate.getDefaultState();

        world.setBlockState(pos, state, 2);
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileCrate) {
            TileCrate tileCrate = (TileCrate)tile;
            tileCrate.setName(crateType.name);
            tileCrate.setShowName(true);
            tileCrate.setLootTable(crateType.pool);
            tileCrate.markDirty();
        }
    }

    /**
     * Use this to rename the crate (but not seal it)
     */
    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent event)
    {
        ItemStack in = event.getItemInput();
        ItemStack combine = event.getIngredientInput();
        ItemStack out = event.getItemResult();
        if (in.isEmpty() || !combine.isEmpty()) return;
        if (!isItemCrate(in)) return;

        event.setBreakChance(0f); // can't break the anvil with naming crates

        NBTTagCompound be = getBlockEntityTag(out);
        be.setString("name", out.getDisplayName());
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();
        if (in.isEmpty() && combine.isEmpty()) return;

        if (isItemCrate(in)) {
            ItemStack out = in.copy();

            // name tag adds the label renderer
            if (combine.getItem() == Items.NAME_TAG) {
                NBTTagCompound be = getBlockEntityTag(out);
                be.setBoolean("showname", true);
                be.setString("name", out.getDisplayName());
            }

            // iron seals the crate
            if (combine.getItem() == Items.IRON_INGOT) {
                String name = in.getDisplayName();
                out = getSealedCrateItem(in, name);
            }

            if (out != null) {
                event.setCost(xpCost);
                event.setOutput(out);
            }
        }
    }

    protected NBTTagCompound getBlockEntityTag(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("BlockEntityTag")) {
            stack.getOrCreateSubCompound("BlockEntityTag");
            tag = stack.getTagCompound();
        }
        return tag.getCompoundTag("BlockEntityTag");
    }

    protected boolean isItemCrate(ItemStack stack)
    {
        return Block.getBlockFromItem(stack.getItem()) instanceof BlockCrate;
    }

    /**
     * Create a TESR for the crate so that a sealed crate's name can be rendered above it.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void preInitClient(FMLPreInitializationEvent event)
    {
        if (showCrateNames) {
            ClientRegistry.bindTileEntitySpecialRenderer(TileCrate.class, new RenderTileCrate());
        }
    }

    /**
     * We need to access the anvil repair event.
     */
    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}