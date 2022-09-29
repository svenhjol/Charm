package svenhjol.charm.module.ender_bundles.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;
import svenhjol.charm.module.ender_bundles.EnderBundles;

@Id("charm:update_ender_inventory")
public class ServerSendUpdateEnderInventory extends ServerSender {
    @Override
    public void send(ServerPlayer player) {
        super.send(player, buf -> {
            var tag = new CompoundTag();
            tag.put(EnderBundles.ENDER_ITEMS_TAG, player.getEnderChestInventory().createTag());
            buf.writeNbt(tag);
        });
    }
}