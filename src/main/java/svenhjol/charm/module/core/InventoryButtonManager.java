package svenhjol.charm.module.core;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import svenhjol.charm.CharmClient;
import svenhjol.charm.api.event.RenderGuiCallback;
import svenhjol.charm.api.event.SetupGuiCallback;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.portable_crafting.PortableCraftingClient;

import java.util.List;

/**
 * Rearrange inventory buttons when recipe button is pressed.
 */
public class InventoryButtonManager extends CharmModule {
    public ImageButton recipeButton;
    public PortableCraftingClient portableCraftingClient;
    private boolean hasHiddenRecipeButton = false;

    @Override
    public void register() {
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    @Override
    public void runWhenEnabled() {
        portableCraftingClient = (PortableCraftingClient) CharmClient.LOADER.getModule("portable_crafting_client");
    }

    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        Screen currentScreen = client.screen;

        if (!(currentScreen instanceof InventoryScreen))
            return;

        if (!buttons.isEmpty() && buttons.get(0) instanceof ImageButton)
            this.recipeButton = (ImageButton)buttons.get(0);

        redrawButtons((InventoryScreen)currentScreen);
    }

    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        Screen currentScreen = client.screen;
        if (!(currentScreen instanceof InventoryScreen))
            return;

        redrawButtons((InventoryScreen)currentScreen);
    }

    private void redrawButtons(InventoryScreen screen) {
        int left = screen.leftPos;

        if (portableCraftingClient != null && portableCraftingClient.isButtonVisible()) {
            // just the recipe and crafting buttons
            if (this.recipeButton != null && this.hasHiddenRecipeButton) {
                this.recipeButton.visible = true;
                this.hasHiddenRecipeButton = false;
            }
            portableCraftingClient.craftingButton.setPosition(left + 76, portableCraftingClient.craftingButton.y);
        }
    }
}