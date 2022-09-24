package svenhjol.charm.module.portable_crafting;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.api.event.RenderGuiCallback;
import svenhjol.charm.api.event.SetupGuiCallback;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.portable_crafting.network.ClientSendOpenCrafting;

import java.util.List;

@ClientModule(module = PortableCrafting.class)
public class PortableCraftingClient extends CharmModule {
    public ImageButton craftingButton;
    public static KeyMapping keyBinding;
    public static ClientSendOpenCrafting CLIENT_SEND_OPEN_CRAFTING;

    @Override
    public void runWhenEnabled() {
        CLIENT_SEND_OPEN_CRAFTING = new ClientSendOpenCrafting();

        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.charm.open_crafting_table",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(level -> {
                if (keyBinding == null || level == null) return;
                while (keyBinding.consumeClick()) {
                    openCraftingTable();
                }
            });
        }
    }

    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        if (client.player == null) return;
        if (!(client.screen instanceof InventoryScreen screen)) return;

        int guiLeft = screen.leftPos;

        this.craftingButton = new ImageButton(guiLeft + 125, height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click
            -> openCraftingTable());

        this.craftingButton.visible = PortableCrafting.hasCraftingTable(client.player);
        screen.addRenderableWidget(this.craftingButton);
    }

    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.screen instanceof InventoryScreen)
            || this.craftingButton == null
            || client.player == null) {
            return;
        }

        if (client.player.level.getGameTime() % 5 == 0) {
            this.craftingButton.visible = PortableCrafting.hasCraftingTable(client.player);
        }
    }

    private void openCraftingTable() {
        CLIENT_SEND_OPEN_CRAFTING.send();
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
