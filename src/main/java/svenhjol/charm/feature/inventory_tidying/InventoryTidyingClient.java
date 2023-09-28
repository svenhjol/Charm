package svenhjol.charm.feature.inventory_tidying;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.event.ScreenRenderEvent;
import svenhjol.charmony_api.event.ScreenSetupEvent;
import svenhjol.charmony_api.iface.IHasScreenOffsetTweaks;
import svenhjol.charmony_api.iface.IInventoryTidyingBlacklistProvider;
import svenhjol.charmony_api.iface.IInventoryTidyingWhitelistProvider;
import svenhjol.charmony_api.iface.IScreenOffsetTweakProvider;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.ScreenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

@ClientFeature
public class InventoryTidyingClient extends CharmFeature
    implements IHasScreenOffsetTweaks, IInventoryTidyingWhitelistProvider, IInventoryTidyingBlacklistProvider {
    private static final int LEFT = 159;
    private static final int TOP = 12;
    private static final List<ImageButton> SORTING_BUTTONS = new ArrayList<>();
    private static final List<Class<? extends Screen>> WHITELISTED_SCREENS = new ArrayList<>();
    private static final List<Class<? extends Screen>> BLACKLISTED_SCREENS = new ArrayList<>();
    private static final Map<Class<? extends Screen>, Pair<Integer, Integer>> SCREEN_TWEAKS = new HashMap<>();
    static final WidgetSprites TIDY_BUTTON = new WidgetSprites(
        Charm.instance().makeId("widget/inventory_tidying/tidy_button"),
        Charm.instance().makeId("widget/inventory_tidying/tidy_button_highlighted")
    );

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(InventoryTidying.class));
    }

    @Override
    public void register() {
        // TODO: IHasBlockEntityScreens and IHasBlacklistedScreens need to be more specific to inventory tidying.
        ApiHelper.consume(IHasScreenOffsetTweaks.class,
            provider -> provider.getScreenOffsetTweaks().forEach(
                tweak -> SCREEN_TWEAKS.put(tweak.getScreen(), tweak.getOffset())));

        ApiHelper.consume(IInventoryTidyingWhitelistProvider.class,
            provider -> WHITELISTED_SCREENS.addAll(provider.getWhitelistedInventoryTidyingScreens()));

        ApiHelper.consume(IInventoryTidyingBlacklistProvider.class,
            provider -> BLACKLISTED_SCREENS.addAll(provider.getBlacklistedInventoryTidyingScreens()));

        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        ScreenSetupEvent.INSTANCE.handle(this::handleScreenSetup);
        ScreenRenderEvent.INSTANCE.handle(this::handleScreenRender);
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
            if (WHITELISTED_SCREENS.contains(containerScreen.getClass()) && slot.index == 0) {
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
        SORTING_BUTTONS.add(new ImageButton(x, y, 10, 10, TIDY_BUTTON, callback));
    }

    private void handleScreenRender(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (BLACKLISTED_SCREENS.contains(screen.getClass())) return;

        // Re-render when recipe is opened/closed.
        var x = ((AbstractContainerScreenAccessor)screen).getLeftPos();
        SORTING_BUTTONS.forEach(button -> button.setPosition(x + LEFT, button.getY()));
    }

    @Override
    public List<IScreenOffsetTweakProvider> getScreenOffsetTweaks() {
        // Offset the button by these X and Y coordinates on these screens.
        return List.of(
            new IScreenOffsetTweakProvider() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return MerchantScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getOffset() {
                    return Pair.of(100, 0);
                }
            },
            new IScreenOffsetTweakProvider() {
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
    public List<Class<? extends Screen>> getBlacklistedInventoryTidyingScreens() {
        // Don't show the button on these screens.
        return List.of(
            CreativeModeInventoryScreen.class,
            BeaconScreen.class
        );
    }

    @Override
    public List<Class<? extends Screen>> getWhitelistedInventoryTidyingScreens() {
        // Add two sorting buttons to screens with a container.
        return List.of(
            ContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            DispenserScreen.class
        );
    }
}
