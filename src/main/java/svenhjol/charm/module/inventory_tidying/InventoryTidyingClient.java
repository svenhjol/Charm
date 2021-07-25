package svenhjol.charm.module.inventory_tidying;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.helper.ScreenRenderHelper;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.PlayerAccessor;
import svenhjol.charm.mixin.accessor.SlotAccessor;
import svenhjol.charm.module.atlases.AtlasScreen;
import svenhjol.charm.module.bookcases.BookcaseScreen;

import java.util.*;

import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.BE;
import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.PLAYER;

@ClientModule(module = InventoryTidying.class)
public class InventoryTidyingClient extends CharmModule {
    public static final int LEFT = 159;
    public static final int TOP = 12;
    public static final List<ImageButton> sortingButtons = new ArrayList<>();

    public final List<Class<? extends Screen>> blockEntityScreens = new ArrayList<>();
    public final List<Class<? extends Screen>> blacklistScreens = new ArrayList<>();

    public final Map<Class<? extends Screen>, Map<Integer, Integer>> screenTweaks = new HashMap<>();

    @Override
    public void register() {
        if (!Charm.LOADER.isEnabled(InventoryTidying.class)) return; // return early, don't even register

        screenTweaks.put(MerchantScreen.class, new HashMap<>() {{ put(100, 0); }});
        screenTweaks.put(InventoryScreen.class, new HashMap<>() {{ put(0, 76); }});

        blockEntityScreens.addAll(Arrays.asList(
            ContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            BookcaseScreen.class,
            DispenserScreen.class
        ));

        blacklistScreens.addAll(Arrays.asList(
            CreativeModeInventoryScreen.class,
            BeaconScreen.class,
            AtlasScreen.class
        ));
    }

    @Override
    public void runWhenEnabled() {
        // set up client listeners
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        if (client.player == null)
            return;

        if (!(client.screen instanceof AbstractContainerScreen))
            return;

        if (blacklistScreens.contains(client.screen.getClass()))
            return;

        sortingButtons.clear();

        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>)client.screen;
        Class<? extends AbstractContainerScreen> clazz = screen.getClass();
        AbstractContainerMenu screenHandler = screen.getMenu();

        int x = ScreenRenderHelper.getX(screen) + LEFT;
        int y = ScreenRenderHelper.getY(screen) - TOP;

        if (screenTweaks.containsKey(clazz)) {
            Map<Integer, Integer> m = screenTweaks.get(clazz);
            for (Map.Entry<Integer, Integer> e : m.entrySet()) {
                x += e.getKey();
                y += e.getValue();
            }
        }

        List<Slot> slots = screenHandler.slots;
        for (Slot slot : slots) {
            if (blockEntityScreens.contains(screen.getClass()) && ((SlotAccessor)slot).accessGetIndex() == 0) {
                this.addSortingButton(screen, x, y + slot.y, click -> sendSortMessage(BE));
            }

            if (slot.container == ((PlayerAccessor)client.player).getInventory()) {
                this.addSortingButton(screen, x, y + slot.y, click -> sendSortMessage(PLAYER));
                break;
            }
        }

        sortingButtons.forEach(screen::addRenderableWidget);
    }

    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (client.screen instanceof InventoryScreen
            && !blacklistScreens.contains(client.screen.getClass())
        ) {
            // handles the recipe being open/closed
            InventoryScreen screen = (InventoryScreen)client.screen;
            int x = ScreenRenderHelper.getX(screen);
            sortingButtons.forEach(button -> button.setPosition(x + LEFT, button.y));
        }
    }

    private void addSortingButton(Screen screen, int x, int y, Button.OnPress onPress) {
        sortingButtons.add(new ImageButton(x, y, 10, 10, 40, 0, 10, CharmResources.INVENTORY_BUTTONS, onPress));
    }

    private void sendSortMessage(int type) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        data.writeInt(type);
        ClientPlayNetworking.send(InventoryTidying.MSG_SERVER_TIDY_INVENTORY, data);
    }
}
