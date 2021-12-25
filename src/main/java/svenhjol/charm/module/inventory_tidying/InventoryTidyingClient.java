package svenhjol.charm.module.inventory_tidying;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.api.event.RenderGuiCallback;
import svenhjol.charm.api.event.SetupGuiCallback;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.inventory_tidying.network.ClientSendTidyInventory;

import java.util.*;

import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.BE;
import static svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler.PLAYER;

@SuppressWarnings("rawtypes")
@ClientModule(module = InventoryTidying.class)
public class InventoryTidyingClient extends CharmModule {
    public static final int LEFT = 159;
    public static final int TOP = 12;
    public static final List<ImageButton> sortingButtons = new ArrayList<>();

    public static ClientSendTidyInventory CLIENT_SEND_TIDY_INVENTORY;

    public final List<Class<? extends Screen>> BLOCK_ENTITY_SCREENS = new ArrayList<>();
    public final List<Class<? extends Screen>> BLACKLIST = new ArrayList<>();
    public final Map<Class<? extends Screen>, Map<Integer, Integer>> SCREEN_TWEAKS = new HashMap<>();

    @Override
    public void register() {
        if (!Charm.LOADER.isEnabled(InventoryTidying.class)) return; // return early, don't even register

        SCREEN_TWEAKS.put(MerchantScreen.class, new HashMap<>() {{ put(100, 0); }});
        SCREEN_TWEAKS.put(InventoryScreen.class, new HashMap<>() {{ put(0, 76); }});

        BLOCK_ENTITY_SCREENS.addAll(Arrays.asList(
            ContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            DispenserScreen.class
        ));

        BLACKLIST.addAll(Arrays.asList(
            CreativeModeInventoryScreen.class,
            BeaconScreen.class
        ));
    }

    @Override
    public void runWhenEnabled() {
        CLIENT_SEND_TIDY_INVENTORY = new ClientSendTidyInventory();
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    private void handleGuiSetup(Minecraft client, int width, int height, List<NarratableEntry> buttons) {
        if (client.player == null) return;
        if (!(client.screen instanceof AbstractContainerScreen<?> screen)) return;
        if (BLACKLIST.contains(client.screen.getClass())) return;

        sortingButtons.clear();

        Class<? extends AbstractContainerScreen> clazz = screen.getClass();
        AbstractContainerMenu screenHandler = screen.getMenu();

        int x = screen.leftPos + LEFT;
        int y = screen.topPos - TOP;

        if (SCREEN_TWEAKS.containsKey(clazz)) {
            Map<Integer, Integer> m = SCREEN_TWEAKS.get(clazz);
            for (Map.Entry<Integer, Integer> e : m.entrySet()) {
                x += e.getKey();
                y += e.getValue();
            }
        }

        List<Slot> slots = screenHandler.slots;
        for (Slot slot : slots) {
            if (BLOCK_ENTITY_SCREENS.contains(screen.getClass()) && slot.index == 0) {
                addSortingButton(screen, x, y + slot.y, click -> CLIENT_SEND_TIDY_INVENTORY.send(BE));
            }

            if (slot.container == client.player.inventory) {
                addSortingButton(screen, x, y + slot.y, click -> CLIENT_SEND_TIDY_INVENTORY.send(PLAYER));
                break;
            }
        }

        sortingButtons.forEach(screen::addRenderableWidget);
    }

    private void handleRenderGui(Minecraft client, PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (client.screen instanceof AbstractContainerScreen screen && !BLACKLIST.contains(screen.getClass())) {
            // handles the recipe being open/closed
            int x = screen.leftPos;
            sortingButtons.forEach(button -> button.setPosition(x + LEFT, button.y));
        }
    }

    private void addSortingButton(Screen screen, int x, int y, Button.OnPress onPress) {
        sortingButtons.add(new ImageButton(x, y, 10, 10, 40, 0, 10, CharmResources.INVENTORY_BUTTONS, onPress));
    }
}
