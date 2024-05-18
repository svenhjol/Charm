package svenhjol.charm.api.iface;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * Specify a list of screens that will not show Charm's ItemTidying button.
 * The blacklist is always prioritised over the whitelist.
 * @see ItemTidyingWhitelistProvider
 */
@SuppressWarnings("unused")
public interface ItemTidyingBlacklistProvider {
    List<Class<? extends Screen>> getBlacklistedItemTidyingScreens();
}
