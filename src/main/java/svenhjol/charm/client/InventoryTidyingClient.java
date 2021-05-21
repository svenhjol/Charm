package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ScreenHelper;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.gui.AtlasScreen;
import svenhjol.charm.gui.BookcaseScreen;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.mixin.accessor.ScreenAccessor;
import svenhjol.charm.mixin.accessor.SlotAccessor;
import svenhjol.charm.module.InventoryTidying;

import java.util.*;
import java.util.function.Consumer;

import static svenhjol.charm.handler.InventoryTidyingHandler.BE;
import static svenhjol.charm.handler.InventoryTidyingHandler.PLAYER;

public class InventoryTidyingClient extends CharmClientModule {
    public static final int LEFT = 159;
    public static final int TOP = 12;
    public static final List<TexturedButtonWidget> sortingButtons = new ArrayList<>();

    public final List<Class<? extends Screen>> blockEntityScreens = new ArrayList<>();
    public final List<Class<? extends Screen>> blacklistScreens = new ArrayList<>();

    public final Map<Class<? extends Screen>, Map<Integer, Integer>> screenTweaks = new HashMap<>();

    public InventoryTidyingClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        if (!module.enabled)
            return;

        screenTweaks.put(MerchantScreen.class, new HashMap<Integer, Integer>() {{ put(100, 0); }});
        screenTweaks.put(InventoryScreen.class, new HashMap<Integer, Integer>() {{ put(0, 76); }});

        blockEntityScreens.addAll(Arrays.asList(
            GenericContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            BookcaseScreen.class,
            Generic3x3ContainerScreen.class
        ));

        blacklistScreens.addAll(Arrays.asList(
            CreativeInventoryScreen.class,
            BeaconScreen.class,
            AtlasScreen.class
        ));
    }

    @Override
    public void init() {
        // set up client listeners
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);
    }

    private void handleGuiSetup(MinecraftClient client, int width, int height, List<ClickableWidget> buttons) {
        if (client.player == null)
            return;

        if (!(client.currentScreen instanceof HandledScreen))
            return;

        if (blacklistScreens.contains(client.currentScreen.getClass()))
            return;

        sortingButtons.clear();

        HandledScreen<?> screen = (HandledScreen<?>)client.currentScreen;
        Class<? extends HandledScreen> clazz = screen.getClass();
        ScreenHandler screenHandler = screen.getScreenHandler();

        int x = ScreenHelper.getX(screen) + LEFT;
        int y = ScreenHelper.getY(screen) - TOP;

        if (screenTweaks.containsKey(clazz)) {
            Map<Integer, Integer> m = screenTweaks.get(clazz);
            for (Map.Entry<Integer, Integer> e : m.entrySet()) {
                x += e.getKey();
                y += e.getValue();
            }
        }

        List<Slot> slots = screenHandler.slots;
        for (Slot slot : slots) {
            if (blockEntityScreens.contains(screen.getClass()) && ((SlotAccessor)slot).getIndex() == 0) {
                this.addSortingButton(screen, x, y + slot.y, click -> sendSortMessage(BE));
            }

            if (slot.inventory == ((PlayerEntityAccessor)client.player).getInventory()) {
                this.addSortingButton(screen, x, y + slot.y, click -> sendSortMessage(PLAYER));
                break;
            }
        }

        sortingButtons.forEach(((ScreenAccessor) screen)::invokeAddButton);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (client.currentScreen instanceof InventoryScreen
            && !blacklistScreens.contains(client.currentScreen.getClass())
        ) {
            // handles the recipe being open/closed
            InventoryScreen screen = (InventoryScreen)client.currentScreen;
            int x = ScreenHelper.getX(screen);
            sortingButtons.forEach(button -> button.setPos(x + LEFT, button.y));
        }
    }

    private void addSortingButton(Screen screen, int x, int y, ButtonWidget.PressAction onPress) {
        sortingButtons.add(new TexturedButtonWidget(x, y, 10, 10, 40, 0, 10, CharmResources.INVENTORY_BUTTONS, onPress));
    }

    private void sendSortMessage(int type) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(type);
        ClientPlayNetworking.send(InventoryTidying.MSG_SERVER_TIDY_INVENTORY, data);
    }
}
