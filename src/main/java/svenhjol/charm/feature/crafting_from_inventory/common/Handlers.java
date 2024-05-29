package svenhjol.charm.feature.crafting_from_inventory.common;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<CraftingFromInventory> {
    public static final Component LABEL = Component.translatable("container.charm.portable_crafting_table");

    public Handlers(CraftingFromInventory feature) {
        super(feature);
    }

    public boolean hasCraftingTable(Player player) {
        for (var provider : feature().providers.inventoryCheckProviders) {
            if (provider.findCraftingTableFromInventory(player).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public void openPortableCraftingReceived(Player player, Networking.C2SOpenPortableCrafting request) {
        if (!hasCraftingTable(player)) return;
        var serverPlayer = (ServerPlayer)player;

        feature().advancements.usedCraftingTable(serverPlayer);
        openContainer(serverPlayer);
    }

    public void openContainer(ServerPlayer player) {
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider(((i, inv, p)
            -> new Menu(i, inv, ContainerLevelAccess.create(p.level(), p.blockPosition()))), LABEL));
    }
}
