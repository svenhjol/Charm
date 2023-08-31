package svenhjol.charm.feature.inventory_tidying;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.event.ScreenRenderEvent;
import svenhjol.charm_api.event.ScreenSetupEvent;
import svenhjol.charm_api.iface.IHasBlacklistedScreens;
import svenhjol.charm_api.iface.IHasBlockEntityScreens;
import svenhjol.charm_api.iface.IHasScreenOffsetTweaks;
import svenhjol.charm_api.iface.IScreenOffsetTweak;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.ApiHelper;
import svenhjol.charm_core.helper.ResourceHelper;
import svenhjol.charm_core.helper.ScreenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

@ClientFeature
public class InventoryTidyingClient extends CharmFeature
    implements IHasScreenOffsetTweaks, IHasBlockEntityScreens, IHasBlacklistedScreens {
    private static final int LEFT = 159;
    private static final int TOP = 12;
    private static final List<ImageButton> SORTING_BUTTONS = new ArrayList<>();
    private static final List<Class<? extends Screen>> BLOCK_ENTITY_SCREENS = new ArrayList<>();
    private static final List<Class<? extends Screen>> BLACKLISTED_SCREENS = new ArrayList<>();
    private static final Map<Class<? extends Screen>, Pair<Integer, Integer>> SCREEN_TWEAKS = new HashMap<>();

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(InventoryTidying.class));
    }

    @Override
    public void register() {
        CharmApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        ScreenSetupEvent.INSTANCE.handle(this::handleScreenSetup);
        ScreenRenderEvent.INSTANCE.handle(this::handleScreenRender);

        ApiHelper.getProviderData(IHasScreenOffsetTweaks.class, provider -> provider.getScreenOffsetTweaks().stream())
            .forEach(t -> SCREEN_TWEAKS.put(t.getScreen(), t.getOffset()));

        BLOCK_ENTITY_SCREENS.addAll(ApiHelper.getProviderData(IHasBlockEntityScreens.class,
            provider -> provider.getBlockEntityScreens().stream()));

        BLACKLISTED_SCREENS.addAll(ApiHelper.getProviderData(IHasBlacklistedScreens.class,
            provider -> provider.getBlacklistedScreens().stream()));
    }

    private void handleScreenSetup(Screen screen) {
        var client = Minecraft.getInstance();

        if (client.player == null) return;
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        if (BLACKLISTED_SCREENS.contains(screen.getClass())) return;

        SORTING_BUTTONS.clear();

        var clazz = containerScreen.getClass();
        var menu = containerScreen.getMenu();
        var x = ((AbstractContainerScreenAccessor)containerScreen).getLeftPos() + LEFT;
        var y = ((AbstractContainerScreenAccessor)containerScreen).getTopPos() - TOP;

        if (SCREEN_TWEAKS.containsKey(clazz)) {
            var pair = SCREEN_TWEAKS.get(clazz);
            x += pair.getFirst();
            y += pair.getSecond();
        }

        var slots = menu.slots;
        for (var slot : slots) {
            if (BLOCK_ENTITY_SCREENS.contains(containerScreen.getClass()) && slot.index == 0) {
                addSortingButton(screen, x, y + slot.y,
                    click -> InventoryTidyingNetwork.TidyInventory.send(TidyType.CONTAINER));
            }

            if (slot.container == client.player.getInventory()) {
                addSortingButton(screen, x, y + slot.y,
                    click -> InventoryTidyingNetwork.TidyInventory.send(TidyType.PLAYER));
                break;
            }
        }

        SORTING_BUTTONS.forEach(b -> ScreenHelper.addRenderableWidget(containerScreen, b));
    }

    private void addSortingButton(Screen screen, int x, int y, Button.OnPress callback) {
        SORTING_BUTTONS.add(new ImageButton(x, y, 10, 10, 40, 0, 10, ResourceHelper.INVENTORY_BUTTONS, callback));
    }

    private void handleScreenRender(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (BLACKLISTED_SCREENS.contains(screen.getClass())) return;

        // Re-render when recipe is opened/closed.
        var x = ((AbstractContainerScreenAccessor)screen).getLeftPos();
        SORTING_BUTTONS.forEach(button -> button.setPosition(x + LEFT, button.getY()));
    }

    @Override
    public List<IScreenOffsetTweak> getScreenOffsetTweaks() {
        // Offset the button by these X and Y coordinates on these screens.
        return List.of(
            new IScreenOffsetTweak() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return MerchantScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getOffset() {
                    return Pair.of(100, 0);
                }
            },
            new IScreenOffsetTweak() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return InventoryScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getOffset() {
                    return Pair.of(0, 76);
                }
            }
        );
    }

    @Override
    public List<Class<? extends Screen>> getBlacklistedScreens() {
        // Don't show the button on these screens.
        return List.of(
            CreativeModeInventoryScreen.class,
            BeaconScreen.class
        );
    }

    @Override
    public List<Class<? extends AbstractContainerScreen<?>>> getBlockEntityScreens() {
        // Add two sorting buttons to screens with a container.
        return List.of(
            ContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            DispenserScreen.class
        );
    }
}
