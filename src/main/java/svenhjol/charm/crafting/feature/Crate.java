package svenhjol.charm.crafting.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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
import svenhjol.meson.MesonItemBlock;
import svenhjol.meson.registry.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.LootHelper;

import java.util.*;

public class Crate extends Feature
{
    public static BlockCrate crate;
    public static BlockCrate crateSealed;
    public static float hardness;
    public static boolean showCrateNames;
    public static int xpCost;
    public static boolean crateTooltip;
    public static boolean requireShift;
    public static ResourceLocation WIDGET_RESOURCE;

    public enum RARITY {
        COMMON,
        UNCOMMON,
        VALUABLE,
        RARE
    }

    public static List<Class<? extends Block>> blacklist = new ArrayList<>();
    public static Map<RARITY, List<CrateType>> types = new HashMap<>();

    public class CrateType
    {
        public String name;
        public String id;
        public ResourceLocation pool;

        public CrateType(String id, ResourceLocation pool)
        {
            this.id = id;
            //noinspection deprecation
            this.name = net.minecraft.util.text.translation.I18n.translateToLocal(id) + " " + net.minecraft.util.text.translation.I18n.translateToLocal("crate");
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
    public void configure()
    {
        super.configure();

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
        crateTooltip = propBoolean(
                "Crate tooltip",
                "Shows tooltip containing crate contents when hovering over the crate in the inventory.",
                true
        );
        requireShift = propBoolean(
                "Tooltip shift key",
                "Crate tooltip needs the shift key held down to be visible",
                false
        );

        // internal
        hardness = 1.0f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        // register the crate and sealed crate blocks
        crate = new BlockCrate(BlockCrate.Type.CRATE);
        crateSealed = new BlockCrate(BlockCrate.Type.CRATE_SEALED);
        GameRegistry.registerTileEntity(crate.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":crate"));

        // blacklist
        blacklist.add(BlockCrate.class);
        blacklist.add(BlockShulkerBox.class);

        // get all loot tables for each rarity type
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

        // create recipes for all crate wood types
        for (int i = 0; i < BlockCrate.WoodVariant.values().length; i++) {
            RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(crate, 1, i),
                "IWI", "W W", "IWI",
                'W', ProxyRegistry.newStack(Blocks.PLANKS, 1, i),
                'I', "ingotIron"
            );
        }
    }

    public static ItemStack getSealedCrateItem(ItemStack in, String name)
    {
        ItemStack out = new ItemStack(Crate.crateSealed, 1, in.getItemDamage());

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

        outTag.getCompoundTag("BlockEntityTag");
        out.setTagCompound(outTag);
        out.setStackDisplayName(name);

        return out;
    }

    public static void generateCrate(World world, BlockPos pos, CrateType crateType, IBlockState state)
    {
        world.setBlockState(pos, state, 2);
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileCrate) {
            TileCrate crate = (TileCrate)tile;
            crate.setName(crateType.name);
            crate.setShowName(true);
            crate.setLootTable(crateType.pool);
            crate.markDirty();
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
        if (in.isEmpty() || !combine.isEmpty()) return;
        if (!isItemCrate(in)) return;

        event.setBreakChance(0f); // can't break the anvil with naming crates
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();
        if (in.isEmpty() && combine.isEmpty()) return;

        if (isItemCrate(in)) {
            ItemStack out = null;
            int cost = 0;

            // name tag adds the label renderer
            if (combine.getItem() == Items.NAME_TAG) {
                out = in.copy();
                cost = in.getCount();

                NBTTagCompound be = getBlockEntityTag(out);
                be.setBoolean("showname", true);
            }

            // iron seals the crate
            if (!isSealedCrate(in) && combine.getItem() == Items.IRON_INGOT) {
                cost = Math.min(in.getCount(), combine.getCount());
                String name = in.getDisplayName();
                out = getSealedCrateItem(in, name);
            }

            if (out != null) {
                out.setCount(cost);
                event.setCost(xpCost);
                event.setMaterialCost(cost);
                event.setOutput(out);
            }
        }
    }

    // some of this is copypasta from Quark ShulkerBoxTooltip
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void makeTooltip(ItemTooltipEvent event)
    {
        if (!event.getItemStack().isEmpty()
            && event.getItemStack().getItem() instanceof MesonItemBlock
            && ((MesonItemBlock) event.getItemStack().getItem()).getBlock() instanceof BlockCrate
            && event.getItemStack().hasTagCompound()
        ) {
            NBTTagCompound tag = ItemNBTHelper.getCompound(event.getItemStack(), "BlockEntityTag");
            if (!tag.isEmpty()) {
                List<String> tooltip = event.getToolTip();
                List<String> tooltipCopy = new ArrayList<>(tooltip);

                for (int i = 1; i < tooltipCopy.size(); i++) {
                    String s = tooltipCopy.get(i);
                    if(!s.startsWith("\u00a7") || s.startsWith("\u00a7o")) {
                        tooltip.remove(s);
                    }
                }

                if (requireShift && !GuiScreen.isShiftKeyDown()) {
                    tooltip.add(1, I18n.format("charm.crate_tooltip_shift"));
                }
            }
        }
    }

    // some of this is copypasta from Quark ShulkerBoxTooltip
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTooltip(RenderTooltipEvent.PostText event)
    {
        if (!event.getStack().isEmpty()
            && event.getStack().getItem() instanceof MesonItemBlock
            && ((MesonItemBlock) event.getStack().getItem()).getBlock() instanceof BlockCrate
            && event.getStack().hasTagCompound()
            && (!requireShift || GuiScreen.isShiftKeyDown())
        ) {
            NBTTagCompound tag = ItemNBTHelper.getCompound(event.getStack(), "BlockEntityTag");
            if (!tag.isEmpty() && tag.hasKey("inventory") && !tag.getCompoundTag("inventory").getTagList("Items", 10).isEmpty()) {
                NonNullList<ItemStack> items = NonNullList.withSize(TileCrate.SIZE, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(tag.getCompoundTag("inventory"), items);

                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution res = new ScaledResolution(mc);

                // only show sealed crates in creative mode
                if (((BlockCrate) ((MesonItemBlock) event.getStack().getItem()).getBlock()).isSealedCrate()
                    && !mc.player.isCreative()) return;

                int x = event.getX() - 5;
                int y = event.getY() - 35;
                int w = 172;
                int h = 64;
                int right = x + w;

                if (y < 0) {
                    y = event.getY() + event.getLines().size() * 10 + 5;
                }

                if (right > res.getScaledWidth()) {
                    x -= (right - res.getScaledWidth());
                }

                GlStateManager.pushMatrix();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();
                GlStateManager.color(1f, 1f, 1f);
                GlStateManager.translate(0, 0, 700);

                mc.getTextureManager().bindTexture(WIDGET_RESOURCE);

                RenderHelper.disableStandardItemLighting();
                Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, 256, 256);
                GlStateManager.color(1f, 1f, 1f);

                RenderItem render = mc.getRenderItem();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableDepth();

                int i = 0;
                for (ItemStack item : items) {
                    int xp = x + 6 + (i % 9) * 18;
                    int yp = y + 6 + (i / 9) * 18;

                    if (!item.isEmpty()) {
                        render.renderItemAndEffectIntoGUI(item, xp, yp);
                        render.renderItemOverlays(mc.fontRenderer, item, xp, yp);
                    }

                    i++;
                }

                GlStateManager.disableDepth();
                GlStateManager.disableRescaleNormal();
                GlStateManager.popMatrix();
            }
        }
    }

    public static boolean canInsertItem(ItemStack stack)
    {
        Class<? extends Block> clazz = Block.getBlockFromItem(stack.getItem()).getClass();
        return !Crate.blacklist.contains(clazz);
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

    protected boolean isSealedCrate(ItemStack stack)
    {
        return isItemCrate(stack) && ((BlockCrate)Block.getBlockFromItem(stack.getItem())).isSealedCrate();
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

    static
    {
        WIDGET_RESOURCE = new ResourceLocation(Charm.MOD_ID, "textures/misc/crate_widget.png");
    }
}