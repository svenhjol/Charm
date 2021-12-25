package svenhjol.charm.module.atlases.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.module.atlases.AtlasInventory;
import svenhjol.charm.module.atlases.Atlases;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

@SuppressWarnings("EnhancedSwitchMigration")
@Id("strange:transfer_atlas")
public class ServerReceiveTransferAtlas extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        if (player == null) return;

        int atlasSlot = buffer.readInt();
        int mapX = buffer.readInt();
        int mapZ = buffer.readInt();
        Atlases.MoveMode mode = buffer.readEnum(Atlases.MoveMode.class);

        server.execute(() -> {
            AtlasInventory inventory = Atlases.getInventory(player.level, player.getInventory().getItem(atlasSlot));

            switch (mode) {
                case TO_HAND:
                    player.containerMenu.setCarried(inventory.removeMapByCoords(player.level, mapX, mapZ).map);
                    Atlases.updateClient(player, atlasSlot);
                    break;
                case TO_INVENTORY:
                    player.addItem(inventory.removeMapByCoords(player.level, mapX, mapZ).map);
                    Atlases.updateClient(player, atlasSlot);
                    break;
                case FROM_HAND:
                    ItemStack heldItem = player.containerMenu.getCarried();
                    if (heldItem.getItem() == Items.FILLED_MAP) {
                        Integer mapId = MapItem.getMapId(heldItem);
                        MapItemSavedData mapState = MapItem.getSavedData(mapId, player.level);
                        if (mapState != null && mapState.scale == inventory.getScale()) {
                            inventory.addToInventory(player.level, heldItem);
                            player.containerMenu.setCarried(ItemStack.EMPTY);
                            Atlases.updateClient(player, atlasSlot);
                        }
                    }
                    break;
                case FROM_INVENTORY:
                    ItemStack stack = player.getInventory().getItem(mapX);
                    if (stack.getItem() == Items.FILLED_MAP) {
                        Integer mapId = MapItem.getMapId(stack);
                        MapItemSavedData mapState = MapItem.getSavedData(mapId, player.level);
                        if (mapState != null && mapState.scale == inventory.getScale()) {
                            inventory.addToInventory(player.level, stack);
                            player.getInventory().removeItemNoUpdate(mapX);
                            Atlases.updateClient(player, atlasSlot);
                        }
                    }
                    break;
            }
        });
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
