package svenhjol.charm.feature.inventory_tidying;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.IContainerOffsetTweak;
import svenhjol.charm.api.iface.IContainerOffsetTweakProvider;
import svenhjol.charm.api.iface.IInventoryTidyingBlacklistProvider;
import svenhjol.charm.api.iface.IInventoryTidyingWhitelistProvider;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.*;

public class InventoryTidyingClient extends ClientFeature
    implements IInventoryTidyingWhitelistProvider, IInventoryTidyingBlacklistProvider, IContainerOffsetTweakProvider {
    static final List<Class<? extends Screen>> WHITELISTED_SCREENS = new ArrayList<>();
    static final List<Class<? extends Screen>> BLACKLISTED_SCREENS = new ArrayList<>();
    static final Map<Class<? extends Screen>, Pair<Integer, Integer>> CONTAINER_OFFSETS = new HashMap<>();

    @Override
    public Class<? extends CommonFeature> commonClass() {
        return InventoryTidying.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        CharmApi.registerProvider(this);
        return Optional.of(new ClientRegistration(this));
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
}
