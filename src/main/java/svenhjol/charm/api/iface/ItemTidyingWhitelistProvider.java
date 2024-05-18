package svenhjol.charm.api.iface;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * Specify a list of screens that will show Charm's ItemTidying button.
 */
@SuppressWarnings("unused")
public interface ItemTidyingWhitelistProvider {
    List<Class<? extends Screen>> getWhitelistedItemTidyingScreens();
}
