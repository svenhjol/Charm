package svenhjol.charm.feature.crafting_from_inventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventoryClient;
import svenhjol.charm.feature.crafting_from_inventory.common.Networking;

public final class Handlers extends FeatureHolder<CraftingFromInventoryClient> {
    public ImageButton craftingButton;

    public Handlers(CraftingFromInventoryClient feature) {
        super(feature);
    }

    public void keyPress(String id) {
        if (Minecraft.getInstance().level != null && id.equals(feature().registers.openPortableCraftingKey.get())) {
            sendOpenPortableCrafting();
        }
    }

    public void screenSetup(Screen screen) {
        if (!(screen instanceof InventoryScreen inventoryScreen)) return;

        var left = inventoryScreen.leftPos;
        var height = inventoryScreen.height;
        var midY = height / 2;

        craftingButton = new ImageButton(left + 127, midY - 22, 20, 18, Buttons.CRAFTING_BUTTON,
            button -> sendOpenPortableCrafting());

        craftingButton.visible = hasCraftingTable();
        inventoryScreen.addRenderableWidget(craftingButton);
    }

    public void screenRender(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (!(screen instanceof InventoryScreen inventoryScreen)) return;

        var level = Minecraft.getInstance().level;
        if (level == null) return;

        if (level.getGameTime() % 5 == 0) {
            craftingButton.visible = hasCraftingTable();
        }

        // Re-render when recipe is opened/closed.
        var x = inventoryScreen.leftPos;
        craftingButton.setPosition(x + 127, craftingButton.getY());
    }

    private void sendOpenPortableCrafting() {
        Networking.C2SOpenPortableCrafting.send();
    }

    private boolean hasCraftingTable() {
        var player = Minecraft.getInstance().player;
        return feature().linked().handlers.hasCraftingTable(player);
    }
}
