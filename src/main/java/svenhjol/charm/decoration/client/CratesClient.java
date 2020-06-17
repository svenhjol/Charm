package svenhjol.charm.decoration.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import svenhjol.charm.Charm;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.VersionHelper;

import java.util.ArrayList;
import java.util.List;

public class CratesClient {
    public static final ResourceLocation WIDGET_RESOURCE = new ResourceLocation(Charm.MOD_ID, "textures/gui/crate_widget.png");

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
                    int h = 27;
                    int right = x + w;

                    if (right > VersionHelper.getMainWindow(mc).getScaledWidth())
                        x -= (right - VersionHelper.getMainWindow(mc).getScaledWidth());

                    if (y < 0) {
                        y = event.getY() + event.getLines().size() * 10 + 5;
                    }

                    RenderSystem.pushMatrix();
                    RenderHelper.enableStandardItemLighting();
                    RenderSystem.enableRescaleNormal();
                    RenderSystem.color3f(1f, 1f, 1f);
                    RenderSystem.translatef(0, 0, 700);
                    mc.getTextureManager().bindTexture(WIDGET_RESOURCE);

                    RenderHelper.disableStandardItemLighting();

//                    drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, 256, 256);
                    renderTooltipBackground(mc, x, y, 9, 1, -1);
                    RenderSystem.color3f(1f, 1f, 1f);

                    ItemRenderer render = mc.getItemRenderer();
                    RenderHelper.enableStandardItemLighting();
                    RenderSystem.enableDepthTest();

                    for (int i = 0; i < size; i++) {
                        ItemStack itemstack;
                        try {
                            itemstack = cap.getStackInSlot(i);
                        } catch (Exception e) {
                            // catch null issue with itemstack. Needs investigation. #255
                            continue;
                        }
                        int xp = x + 6 + (i % 9) * 18;
                        int yp = y + 6 + (i / 9) * 18;

                        if (!itemstack.isEmpty()) {
                            render.renderItemAndEffectIntoGUI(itemstack, xp, yp);
                            render.renderItemOverlays(mc.fontRenderer, itemstack, xp, yp);
                        }
                    }

                    RenderSystem.disableDepthTest();
                    RenderSystem.disableRescaleNormal();
                    RenderSystem.popMatrix();
                });
            }
        }
    }

    private boolean isCrate(ItemStack stack) {
        final ResourceLocation itemRegName = stack.getItem().getRegistryName();
        if (itemRegName == null)
            return false;

        final String itemName = itemRegName.toString();
        // OH JUST GO TO BED SVEN
        return itemName.contains("charm:crate_") || itemName.contains("covalent:crate_");
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

    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;

    // copypasta from Quark ShulkerBoxTooltips#renderTooltipBackground()
    // also copied shulker_widget.png to resources/assets/textures/gui/misc/crate_widget.png
    // When using the default 1.14 method, the background draws poorly. This method is much better.
    public static void renderTooltipBackground(Minecraft mc, int x, int y, int width, int height, int color) {
        mc.getTextureManager().bindTexture(WIDGET_RESOURCE);
        RenderSystem.color3f(((color & 0xFF0000) >> 16) / 255f,
            ((color & 0x00FF00) >> 8) / 255f,
            (color & 0x0000FF) / 255f);

        RenderHelper.disableStandardItemLighting();

        AbstractGui.blit(x, y,
            0, 0,
            CORNER, CORNER, 256, 256);
        AbstractGui.blit(x + CORNER + EDGE * width, y + CORNER + EDGE * height,
            CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        AbstractGui.blit(x + CORNER + EDGE * width, y,
            CORNER + BUFFER + EDGE + BUFFER, 0,
            CORNER, CORNER, 256, 256);
        AbstractGui.blit(x, y + CORNER + EDGE * height,
            0, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        for (int row = 0; row < height; row++) {
            AbstractGui.blit(x, y + CORNER + EDGE * row,
                0, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            AbstractGui.blit(x + CORNER + EDGE * width, y + CORNER + EDGE * row,
                CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            for (int col = 0; col < width; col++) {
                if (row == 0) {
                    AbstractGui.blit(x + CORNER + EDGE * col, y,
                        CORNER + BUFFER, 0,
                        EDGE, CORNER, 256, 256);
                    AbstractGui.blit(x + CORNER + EDGE * col, y + CORNER + EDGE * height,
                        CORNER + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
                        EDGE, CORNER, 256, 256);
                }

                AbstractGui.blit(x + CORNER + EDGE * col, y + CORNER + EDGE * row,
                    CORNER + BUFFER, CORNER + BUFFER,
                    EDGE, EDGE, 256, 256);
            }
        }

        RenderSystem.color3f(1F, 1F, 1F);
    }
}
