package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.base.helper.ScreenHelper;
import svenhjol.charm.event.GuiSetupCallback;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.module.PortableCrafting;

import java.util.List;
import java.util.function.Consumer;

public class PortableCraftingClient extends CharmClientModule {
    public TexturedButtonWidget craftingButton;
    public static KeyBinding keyBinding;

    public PortableCraftingClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // set up client listeners
        GuiSetupCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.charm.openCraftingTable",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(world -> {
                if (keyBinding == null || world == null)
                    return;

                while (keyBinding.wasPressed()) {
                    triggerOpenCraftingTable();
                }
            });
        }
    }

    private void handleGuiSetup(MinecraftClient client, int width, int height, List<AbstractButtonWidget> buttons, Consumer<AbstractButtonWidget> addButton) {
        if (client.player == null)
            return;

        if (!(client.currentScreen instanceof InventoryScreen))
            return;

        InventoryScreen screen = (InventoryScreen)client.currentScreen;
        int guiLeft = ScreenHelper.getX(screen);

        this.craftingButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            triggerOpenCraftingTable();
        });

        this.craftingButton.visible = hasCrafting(client.player);
        addButton.accept(this.craftingButton);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.currentScreen instanceof InventoryScreen)
            || this.craftingButton == null
            || client.player == null
        ) {
            return;
        }

        if (client.player.world.getTime() % 5 == 0)
            this.craftingButton.visible = hasCrafting(client.player);
    }

    private boolean hasCrafting(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getInventory().contains(new ItemStack(Blocks.CRAFTING_TABLE));
    }

    private void triggerOpenCraftingTable() {
        ClientSidePacketRegistry.INSTANCE.sendToServer(PortableCrafting.MSG_SERVER_OPEN_CRAFTING, new PacketByteBuf(Unpooled.buffer()));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
