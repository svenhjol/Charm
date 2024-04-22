package svenhjol.charm.api.iface;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * Specify a list of screens that will show Charm's InventoryTidying button.
 */
@SuppressWarnings("unused")
public interface IInventoryTidyingWhitelistProvider {
    List<Class<? extends Screen>> getWhitelistedInventoryTidyingScreens();
}
