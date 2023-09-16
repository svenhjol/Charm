package svenhjol.charm.feature.proximity_workstations.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsClient;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenSpecificWorkstation;
import svenhjol.charmony.helper.TextHelper;

import java.util.List;

public class SelectWorkstationScreen extends Screen {
    private final int backgroundWidth;
    private final int backgroundHeight;
    private final List<Block> blocks;

    public SelectWorkstationScreen(List<Block> blocks) {
        super(TextHelper.translatable("gui.charm.workstation_selector.title"));

        this.blocks = blocks;
        this.backgroundWidth = 181;
        this.backgroundHeight = 66;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.render(guiGraphics, mouseX, mouseY, tickDelta);
        renderBackground(guiGraphics);

        var midX = width / 2;
        var midY = height / 2;
        var size = blocks.size();

        renderLabels(guiGraphics, midX, midY);

        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);
            var stack = new ItemStack(block);
            var xpos = midX - ((size * 22) / 2) + (i * 22) - 1;
            var ypos = midY - 9;
            guiGraphics.renderItem(stack, xpos, ypos);
            guiGraphics.drawString(this.font, String.valueOf(i + 1), xpos + 6, ypos + 23, 0x404040, false);
        }
    }

    @Override
    public boolean keyReleased(int val, int j, int k) {
        if (val >= 49 && val <= 58) {
            var num = val - 49;
            if (num <= (blocks.size() - 1)) {
                try {
                    openWorkstation(blocks.get(num));
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }
        }
        return super.keyReleased(val, j, k);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        var midX = width / 2;
        var midY = height / 2;
        var size = blocks.size();

        for (int i = 0; i < blocks.size(); i++) {
            try {
                var block = blocks.get(i);
                var xpos = midX - ((size * 22) / 2) + (i * 22) - 1;
                var ypos = midY - 9;

                if (x >= xpos && x <= xpos + 16
                    && y >= ypos && y <= ypos + 16) {
                    openWorkstation(block);
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
                // --
            }
        }

        return super.mouseClicked(x, y, button);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        var midX = (width - backgroundWidth) / 2;
        var midY = (height - backgroundHeight) / 2;

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.blit(ProximityWorkstationsClient.selectWorkstationScreenBackground,
            midX, midY, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int midX, int midY) {
        guiGraphics.drawString(font, title, midX - 4 - ((title.getString().length() * 5) / 2), midY - 25, 0x404040, false);
    }

    protected void openWorkstation(Block block) {
        OpenSpecificWorkstation.send(block);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return super.keyPressed(i, j, k);
    }
}
