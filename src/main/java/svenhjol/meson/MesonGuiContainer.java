package svenhjol.meson;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class MesonGuiContainer extends GuiContainer
{
    private InventoryPlayer playerInv;
    private String displayName;
    private ResourceLocation BG_TEXTURE;

    public MesonGuiContainer(String displayName, Container container, InventoryPlayer playerInv, ResourceLocation texture)
    {
        super(container);
        this.playerInv = playerInv;
        this.displayName = I18n.format(displayName);
        BG_TEXTURE = texture;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        // set to white
        GlStateManager.color(1, 1, 1, 1);

        // bind texture so that when rect drawn it draws with this texture
        mc.getTextureManager().bindTexture(BG_TEXTURE);

        // centre on the screen
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        // draw the rect with the texture
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRenderer.drawString(displayName, 8, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    }
}
