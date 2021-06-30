package svenhjol.charm.module.inventory_tidying;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.handler.ClientModuleHandler;
import svenhjol.charm.helper.ScreenHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.portable_crafting.PortableCraftingClient;

import java.util.List;

public class InventoryButtonClient extends CharmClientModule {
    public ImageButton recipeButton;
    public PortableCraftingClient portableCraftingClient;
    private boolean hasHiddenRecipeButton = false;

    public InventoryButtonClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    @Override
    public void init() {
        portableCraftingClient = (PortableCraftingClient) ClientModuleHandler.INSTANCE.getModule("portable_crafting_client");
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
        int left = ScreenHelper.getX(screen);

        if (portableCraftingClient != null && portableCraftingClient.isButtonVisible()) {
            // just the recipe and crafting buttons
            if (this.recipeButton != null && this.hasHiddenRecipeButton) {
                this.recipeButton.visible = true;
                this.hasHiddenRecipeButton = false;
            }
            portableCraftingClient.craftingButton.setPosition(left + 130, portableCraftingClient.craftingButton.y);
        }
    }
}
