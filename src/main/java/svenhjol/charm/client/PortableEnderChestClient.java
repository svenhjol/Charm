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
import svenhjol.charm.base.CharmResources;
import svenhjol.charm.event.GuiSetupCallback;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.module.PortableEnderChest;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ScreenHelper;

import java.util.List;
import java.util.function.Consumer;

public class PortableEnderChestClient extends CharmClientModule {
    public TexturedButtonWidget chestButton;
    public static KeyBinding keyBinding;

    public PortableEnderChestClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // set up client listeners
        GuiSetupCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        if (PortableEnderChest.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.charm.openEnderChest",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(world -> {
                if (keyBinding == null || world == null)
                    return;

                while (keyBinding.wasPressed()) {
                    triggerOpenChest();
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

        this.chestButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            triggerOpenChest();
        });

        this.chestButton.visible = hasChest(client.player);
        addButton.accept(this.chestButton);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.currentScreen instanceof InventoryScreen)
            || this.chestButton == null
            || client.player == null
        ) {
            return;
        }

        if (client.player.world.getTime() % 5 == 0)
            this.chestButton.visible = hasChest(client.player);
    }

    private boolean hasChest(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getInventory().contains(new ItemStack(Blocks.ENDER_CHEST));
    }

    private void triggerOpenChest() {
        ClientSidePacketRegistry.INSTANCE.sendToServer(PortableEnderChest.MSG_SERVER_OPEN_ENDER_CHEST, new PacketByteBuf(Unpooled.buffer()));
    }

    public boolean isButtonVisible() {
        return this.chestButton != null && this.chestButton.visible;
    }
}
