package svenhjol.charm.module.inventory_tidying;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ClientHandler;
import svenhjol.charm.helper.ScreenHelper;
import svenhjol.charm.module.portable_crafting.PortableCraftingClient;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.event.RenderGuiCallback;

import java.util.List;

public class InventoryButtonClient extends CharmClientModule {
    public TexturedButtonWidget recipeButton;
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
        portableCraftingClient = (PortableCraftingClient) ClientHandler.getModule("portable_crafting");
    }

    private void handleGuiSetup(MinecraftClient client, int width, int height, List<Selectable> buttons) {
        Screen currentScreen = client.currentScreen;

        if (!(currentScreen instanceof InventoryScreen))
            return;

        if (!buttons.isEmpty() && buttons.get(0) instanceof TexturedButtonWidget)
            this.recipeButton = (TexturedButtonWidget)buttons.get(0);

        redrawButtons((InventoryScreen)currentScreen);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Screen currentScreen = client.currentScreen;
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
            portableCraftingClient.craftingButton.setPos(left + 130, portableCraftingClient.craftingButton.y);
        }
    }
}
