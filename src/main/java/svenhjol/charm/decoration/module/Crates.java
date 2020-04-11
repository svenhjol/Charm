package svenhjol.charm.decoration.module;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.CrateBaseBlock;
import svenhjol.charm.decoration.block.CrateOpenBlock;
import svenhjol.charm.decoration.block.CrateSealedBlock;
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.decoration.inventory.CrateScreen;
import svenhjol.charm.decoration.tileentity.CrateTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.VersionHelper;
import svenhjol.meson.iface.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION, hasSubscriptions = true,
    description = "A smaller storage solution with the benefit of being transportable.\n" +
        "You can also seal a crate by combining it with an iron ingot on an anvil.  The only way to get things out is to break it.")
public class Crates extends MesonModule {
    public static Map<WoodType, CrateOpenBlock> openTypes = new HashMap<>();
    public static Map<WoodType, CrateSealedBlock> sealedTypes = new HashMap<>();
    public static List<Class<? extends Block>> invalidBlocks = new ArrayList<>();
    public static List<Class<? extends Item>> invalidItems = new ArrayList<>();

    @ObjectHolder("charm:crate")
    public static ContainerType<CrateContainer> container;

    @ObjectHolder("charm:crate")
    public static TileEntityType<CrateTileEntity> tile;

    public static final ResourceLocation WIDGET_RESOURCE = new ResourceLocation(Charm.MOD_ID, "textures/gui/crate_overlay.png");

    @SuppressWarnings("ConstantConditions")
    @Override
    public void init() {
        // create all wood types for open and sealed crates
        for (WoodType wood : WoodType.values()) {
            openTypes.put(wood, new CrateOpenBlock(this, wood));
            sealedTypes.put(wood, new CrateSealedBlock(this, wood));
        }

        // add invalid blocks
        invalidBlocks.add(ShulkerBoxBlock.class);
        invalidBlocks.add(CrateOpenBlock.class);
        invalidBlocks.add(CrateSealedBlock.class);

        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "crate");

        container = new ContainerType<>(CrateContainer::instance);
        tile = TileEntityType.Builder.create(CrateTileEntity::new).build(null);

        RegistryHandler.registerContainer(container, res);
        RegistryHandler.registerTile(tile, res);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(container, CrateScreen::new);
    }

    public static boolean canInsertItem(ItemStack stack) {
        Class<? extends Block> blockClass = Block.getBlockFromItem(stack.getItem()).getClass();
        return !invalidItems.contains(stack.getItem().getClass())
            && !invalidBlocks.contains(blockClass);
    }

    @SubscribeEvent
    public void onItemToolTip(ItemTooltipEvent event) {
        final ItemStack stack = event.getItemStack();
        if (!isCrate(stack) || !stack.hasTag())
            return;

        CompoundNBT tag = ItemNBTHelper.getCompound(stack, "BlockEntityTag", true);
        if (tag != null) {
            if (!tag.contains("id", Constants.NBT.TAG_STRING)) {
                tag = tag.copy();
                tag.putString("id", "charm:crate");
            }
            TileEntity tile = TileEntity.create(tag);
            if (tile != null && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                List<ITextComponent> toolTip = event.getToolTip();
                List<ITextComponent> toolTipCopy = new ArrayList<>(toolTip);

                for (int i = 1; i < toolTipCopy.size(); i++) {
                    final ITextComponent t = toolTipCopy.get(i);
                    final String s = t.getFormattedText();

                    // shamelessly lifted from Quark
                    if (!s.startsWith("\u00a7") || s.startsWith("\u00a7o"))
                        toolTip.remove(t);
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderToolTip(RenderTooltipEvent.PostText event) {
        final Minecraft mc = Minecraft.getInstance();
        final ItemStack stack = event.getStack();
        if (!isCrate(stack) || !stack.hasTag())
            return;

        CompoundNBT tag = ItemNBTHelper.getCompound(stack, "BlockEntityTag", true);
        if (tag != null) {
            if (!tag.contains("id", Constants.NBT.TAG_STRING)) {
                tag = tag.copy();
                tag.putString("id", "charm:crate");
            }
            TileEntity tile = TileEntity.create(tag);
            if (tile != null) {
                final LazyOptional<IItemHandler> handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                handler.ifPresent(cap -> {
                    int size = cap.getSlots();

                    int x = event.getX() - 5;
                    int y = event.getY() - 35;
                    int w = 172;
                    int h = 64;
                    int right = x + w;

                    if (right > VersionHelper.getMainWindow(mc).getScaledWidth())
                        x -= (right - VersionHelper.getMainWindow(mc).getScaledWidth());

                    if (y < 0) {
                        y = event.getY() + event.getLines().size() * 10 + 5;
                    }

                    GlStateManager.pushMatrix();
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.color3f(1f, 1f, 1f);
                    GlStateManager.translatef(0, 0, 700);
                    mc.getTextureManager().bindTexture(WIDGET_RESOURCE);

                    RenderHelper.disableStandardItemLighting();

                    drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, 256, 256);
                    GlStateManager.color3f(1f, 1f, 1f);

                    ItemRenderer render = mc.getItemRenderer();
                    RenderHelper.enableGUIStandardItemLighting();
                    GlStateManager.enableDepthTest();

                    for (int i = 0; i < size; i++) {
                        ItemStack itemstack = cap.getStackInSlot(i);
                        int xp = x + 6 + (i % 9) * 18;
                        int yp = y + 6 + (i / 9) * 18;

                        if (!itemstack.isEmpty()) {
                            render.renderItemAndEffectIntoGUI(itemstack, xp, yp);
                            render.renderItemOverlays(mc.fontRenderer, itemstack, xp, yp);
                        }
                    }

                    GlStateManager.disableDepthTest();
                    GlStateManager.disableRescaleNormal();
                    GlStateManager.popMatrix();
                });
            }
        }
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out = event.getOutput();
        if (left.isEmpty() || right.isEmpty()) return;

        Block block = Block.getBlockFromItem(left.getItem());
        if (!(block instanceof CrateBaseBlock)) return;

        if (right.getItem() == Items.IRON_INGOT && block instanceof CrateOpenBlock) {
            WoodType wood = ((CrateOpenBlock) block).getWood();

            out = new ItemStack(sealedTypes.get(wood));
            out.setTag(left.getTag());
            out.setDisplayName(left.getDisplayName());
        }

        if (!out.isEmpty()) {
            event.setOutput(out);
            event.setCost(1);
            event.setMaterialCost(1);
        }
    }

    private boolean isCrate(ItemStack stack) {
        final ResourceLocation itemRegName = stack.getItem().getRegistryName();
        if (itemRegName == null)
            return false;

        final String itemName = itemRegName.toString();
        if (!itemName.contains("charm:crate_"))
            return false;

        return true;
    }

    public void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, (y + height), 0.0D).tex((u * f), ((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((u + (float)width) * f), ((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((x + width), y, 0.0D).tex(((u + (float)width) * f), (v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();
    }
}
