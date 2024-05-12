package svenhjol.charm.feature.inventory_tidying.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import svenhjol.charm.api.iface.IContainerOffsetTweak;
import svenhjol.charm.api.iface.IContainerOffsetTweakProvider;
import svenhjol.charm.api.iface.IInventoryTidyingBlacklistProvider;
import svenhjol.charm.api.iface.IInventoryTidyingWhitelistProvider;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingClient;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Providers extends ProviderHolder<InventoryTidyingClient>
    implements IInventoryTidyingWhitelistProvider, IInventoryTidyingBlacklistProvider, IContainerOffsetTweakProvider {
    public final List<Class<? extends Screen>> whitelistedScreens = new ArrayList<>();
    public final List<Class<? extends Screen>> blacklistedScreens = new ArrayList<>();
    public final Map<Class<? extends Screen>, Pair<Integer, Integer>> containerOffsets = new HashMap<>();

    public Providers(InventoryTidyingClient feature) {
        super(feature);
    }

    @Override
    public List<IContainerOffsetTweak> getContainerOffsetTweaks() {
        // Offset the button by these X and Y coordinates on these screens.
        return List.of(
            new IContainerOffsetTweak() {
                @Override
                public Class<? extends Screen> getScreen() {
                    return MerchantScreen.class;
                }

                @Override
                public Pair<Integer, Integer> getOffset() {
                    return Pair.of(100, 0);
                }
            },
            new IContainerOffsetTweak() {
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

    @Override
    public void onEnabled() {
        ApiHelper.consume(IContainerOffsetTweakProvider.class,
            provider -> provider.getContainerOffsetTweaks().forEach(
                tweak -> containerOffsets.put(tweak.getScreen(), tweak.getOffset())));

        ApiHelper.consume(IInventoryTidyingWhitelistProvider.class,
            provider -> whitelistedScreens.addAll(provider.getWhitelistedInventoryTidyingScreens()));

        ApiHelper.consume(IInventoryTidyingBlacklistProvider.class,
            provider -> blacklistedScreens.addAll(provider.getBlacklistedInventoryTidyingScreens()));
    }
}
