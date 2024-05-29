package svenhjol.charm.feature.item_tidying.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import svenhjol.charm.api.iface.ContainerOffsetTweak;
import svenhjol.charm.api.iface.ContainerOffsetTweakProvider;
import svenhjol.charm.api.iface.ItemTidyingBlacklistProvider;
import svenhjol.charm.api.iface.ItemTidyingWhitelistProvider;
import svenhjol.charm.feature.item_tidying.ItemTidyingClient;
import svenhjol.charm.charmony.feature.ProviderHolder;
import svenhjol.charm.charmony.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Providers extends ProviderHolder<ItemTidyingClient>
    implements ItemTidyingWhitelistProvider, ItemTidyingBlacklistProvider, ContainerOffsetTweakProvider {
    public final List<Class<? extends Screen>> whitelistedScreens = new ArrayList<>();
    public final List<Class<? extends Screen>> blacklistedScreens = new ArrayList<>();
    public final Map<Class<? extends Screen>, Pair<Integer, Integer>> containerOffsets = new HashMap<>();

    public Providers(ItemTidyingClient feature) {
        super(feature);
    }

    @Override
    public List<ContainerOffsetTweak> getContainerOffsetTweaks() {
        // Offset the button by these X and Y coordinates on these screens.
        return List.of(
            new ContainerOffsetTweak() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return MerchantScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getOffset() {
                    return Pair.of(100, 0);
                }
            },
            new ContainerOffsetTweak() {
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
    public List<Class<? extends Screen>> getBlacklistedItemTidyingScreens() {
        // Don't show the button on these screens.
        return List.of(
            CreativeModeInventoryScreen.class,
            BeaconScreen.class
        );
    }

    @Override
    public List<Class<? extends Screen>> getWhitelistedItemTidyingScreens() {
        // Add two sorting buttons to screens with a container.
        return List.of(
            ContainerScreen.class,
            HopperScreen.class,
            ShulkerBoxScreen.class,
            DispenserScreen.class
        );
    }

    @Override
    public void onEnabled() {
        Api.consume(ContainerOffsetTweakProvider.class,
            provider -> provider.getContainerOffsetTweaks().forEach(
                tweak -> containerOffsets.put(tweak.getScreen(), tweak.getOffset())));

        Api.consume(ItemTidyingWhitelistProvider.class,
            provider -> whitelistedScreens.addAll(provider.getWhitelistedItemTidyingScreens()));

        Api.consume(ItemTidyingBlacklistProvider.class,
            provider -> blacklistedScreens.addAll(provider.getBlacklistedItemTidyingScreens()));
    }
}
