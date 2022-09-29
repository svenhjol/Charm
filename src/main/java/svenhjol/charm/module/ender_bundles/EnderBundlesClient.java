package svenhjol.charm.module.ender_bundles;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.api.event.ItemTooltipImageCallback;
import svenhjol.charm.api.event.RenderGuiCallback;
import svenhjol.charm.api.event.RenderTooltipCallback;
import svenhjol.charm.api.event.SetupGuiCallback;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.portable_crafting.PortableCrafting;
import svenhjol.charm.module.ender_bundles.network.ClientReceiveUpdateEnderInventory;
import svenhjol.charm.module.ender_bundles.network.ClientSendOpenEnderBundle;
import svenhjol.charm.module.ender_bundles.network.ClientSendRequestEnderInventory;

import java.util.List;
import java.util.Optional;

@ClientModule(module = EnderBundles.class)
public class EnderBundlesClient extends CharmModule {
    public static KeyMapping keyBinding;
    public ImageButton enderButton;
    public static float CACHED_AMOUNT_FILLED = 0.0F;
    public static final int ITEM_BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    
    private static final ClientSendOpenEnderBundle SEND_OPEN_ENDER_BUNDLE = new ClientSendOpenEnderBundle();
    private static final ClientSendRequestEnderInventory SEND_REQUEST_ENDER_INVENTORY = new ClientSendRequestEnderInventory();
    private static final ClientReceiveUpdateEnderInventory RECEIVE_UPDATE_ENDER_INVENTORY = new ClientReceiveUpdateEnderInventory();
    
    @Override
    public void runWhenEnabled() {
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
        ItemTooltipImageCallback.EVENT.register(this::handleItemTooltipImage);
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    
        // Set up item predicate so the icon changes when full.
        ItemProperties.register(EnderBundles.ENDER_BUNDLE, new ResourceLocation("ender_bundle_filled"), (stack, world, entity, i)
            -> EnderBundleItem.getAmountFilled());
        
        if (EnderBundles.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.charm.open_ender_bundle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.categories.inventory"
            ));
    
            ClientTickEvents.END_WORLD_TICK.register(level -> {
                if (keyBinding == null || level == null) return;
                while (keyBinding.consumeClick()) {
                    openEnderBundle();
                }
            });
        }
    }
    
    public static void handleReceiveUpdateEnderInventory(LocalPlayer player, CompoundTag tag) {
        if (tag != null && tag.contains(EnderBundles.ENDER_ITEMS_TAG)) {
            var enderItems = tag.getList(EnderBundles.ENDER_ITEMS_TAG, 10);
            var inventory = player.getEnderChestInventory();
            inventory.fromTag(enderItems);
        
            CACHED_AMOUNT_FILLED = (float)enderItems.size() / inventory.getContainerSize();
        }
    }
    
    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        if (client.player == null) return;
        if (!(client.screen instanceof InventoryScreen screen)) return;
        
        int guiLeft = screen.leftPos;
        int midY = height / 2;
        midY += PortableCrafting.hasCraftingTable(client.player) ? 18 : 0;
        
        this.enderButton = new ImageButton(guiLeft + 76, midY - 66, 20, 18, 20, 0, 19, CharmResources.INVENTORY_BUTTONS, click
            -> openEnderBundle());
        
        this.enderButton.visible = EnderBundles.hasEnderBundle(client.player);
        screen.addRenderableWidget(this.enderButton);
    }
    
    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.screen instanceof InventoryScreen)
            || this.enderButton == null
            || client.player == null) {
            return;
        }
        
        if (client.player.level.getGameTime() % 5 == 0) {
            this.enderButton.visible = EnderBundles.hasEnderBundle(client.player);
        }
    }
    
    private void openEnderBundle() {
        SEND_OPEN_ENDER_BUNDLE.send();
    }
    
    private void requestEnderInventory() {
        SEND_REQUEST_ENDER_INVENTORY.send();
    }
    
    /**
     * Poll for Ender Inventory changes on the server.
     */
    private void handleClientTick(Minecraft client) {
        if (client == null || client.level == null || client.player == null) {
            return;
        }
        
        // Do this sparingly.
        if (client.level.getGameTime() % 60 == 0) {
            requestEnderInventory();
        }
    }
    
    /**
     * When hovering, remove all text below the custom grid.
     */
    private void handleRenderTooltip(Screen screen, PoseStack poseStack, ItemStack stack, List<ClientTooltipComponent> components, int x, int y) {
        if (stack != null && stack.is(EnderBundles.ENDER_BUNDLE)) {
            var client = Minecraft.getInstance();
            if (client != null && client.level != null && client.level.getGameTime() % 5 == 0) {
                requestEnderInventory();
            }
    
            if (components.size() >= 2) {
                ClientTooltipComponent title = components.get(0);
                ClientTooltipComponent icons = components.get(1);
                components.clear();
                components.add(0, title);
                components.add(1, icons);
            }
        }
    }
    
    /**
     * Add code to the getTooltipImage method to display our custom grid.
     */
    private Optional<TooltipComponent> handleItemTooltipImage(ItemStack stack) {
        if (stack != null && stack.is(EnderBundles.ENDER_BUNDLE)) {
            var client = Minecraft.getInstance();
            if (client == null || client.player == null) return Optional.empty();
    
            var items = EnderBundles.getEnderInventory(client.player);
            return Optional.of(new EnderBundleItemTooltip(items));
        }
        
        return Optional.empty();
    }
}