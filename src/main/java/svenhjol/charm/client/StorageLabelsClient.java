package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.module.StorageLabels;

public class StorageLabelsClient extends CharmClientModule {
    public StorageLabelsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_UPDATE_CUSTOM_NAME, this::handleUpdateCustomName);
    }

    private void handleUpdateCustomName(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        String label = data.readString();

        client.execute(() -> {
            ClientHelper.getWorld().ifPresent(world -> {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof LootableContainerBlockEntity) {
                    LootableContainerBlockEntity container = (LootableContainerBlockEntity) blockEntity;
                    container.setCustomName(new LiteralText(label));
                }
            });
        });
    }
}
