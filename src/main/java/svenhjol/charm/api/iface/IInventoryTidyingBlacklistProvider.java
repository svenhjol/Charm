package svenhjol.charm.api.iface;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * Specify a list of screens that will not show Charm's InventoryTidying button.
 * The blacklist is always prioritised over the whitelist.
 * @see IInventoryTidyingWhitelistProvider
 */
@SuppressWarnings("unused")
public interface IInventoryTidyingBlacklistProvider {
    List<Class<? extends Screen>> getBlacklistedInventoryTidyingScreens();
}
