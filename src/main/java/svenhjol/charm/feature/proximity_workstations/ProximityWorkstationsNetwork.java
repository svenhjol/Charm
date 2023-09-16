package svenhjol.charm.feature.proximity_workstations;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.Packet;
import svenhjol.charmony.enums.PacketDirection;
import svenhjol.charmony.iface.IPacketRequest;

import java.util.LinkedList;
import java.util.List;

public class ProximityWorkstationsNetwork {
    public static void register() {
        var registry = Charm.instance().registry();

        registry.packet(new OpenWorkstationSelector(),
            () -> ProximityWorkstations::handleOpenedSelector);

        registry.packet(new OpenSpecificWorkstation(),
            () -> ProximityWorkstations::handleOpenedSpecificWorkstation);

        registry.packet(new OpenWorkstationSelectorScreen(),
            () -> ProximityWorkstationsClient::handleOpenedSelectorScreen);
    }

    @Packet(
        id = "charm:open_workstation_selector",
        direction = PacketDirection.CLIENT_TO_SERVER,
        description = "An empty packet sent from the client to instruct the server to open the workstation selector."
    )
    public static class OpenWorkstationSelector implements IPacketRequest {
        private OpenWorkstationSelector() {}

        public static void send() {
            CharmClient.instance().network().send(new OpenWorkstationSelector());
        }
    }

    @Packet(
        id = "charm:open_specific_workstation",
        direction = PacketDirection.CLIENT_TO_SERVER,
        description = "Workstation block sent from client to instruct the server to open a specific container."
    )
    public static class OpenSpecificWorkstation implements IPacketRequest {
        private Block workstation;

        private OpenSpecificWorkstation() {}

        public static void send(Block workstation) {
            var message = new OpenSpecificWorkstation();
            message.workstation = workstation;
            CharmClient.instance().network().send(message);
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            var key = BuiltInRegistries.BLOCK.getKey(workstation);
            buf.writeUtf(key.toString());
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            var key = buf.readUtf();
            workstation = BuiltInRegistries.BLOCK.get(new ResourceLocation(key));
        }

        public Block getWorkstation() {
            return workstation;
        }
    }

    @Packet(
        id = "charm:open_workstation_selector_screen",
        direction = PacketDirection.SERVER_TO_CLIENT,
        description = "List of workstation blocks sent from the server to be shown on the workstation selection screen."
    )
    public static class OpenWorkstationSelectorScreen implements IPacketRequest {
        private List<Block> workstations = new LinkedList<>();

        private OpenWorkstationSelectorScreen() {}

        public static void send(Player player, List<Block> workstations) {
            var message = new OpenWorkstationSelectorScreen();
            message.workstations = workstations;
            Charm.instance().network().send(message, player);
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            var blockList = new ListTag();

            for (var workstation : workstations) {
                var key = BuiltInRegistries.BLOCK.getKey(workstation);
                blockList.add(StringTag.valueOf(key.toString()));
            }

            var nbt = new CompoundTag();
            nbt.put("workstations", blockList);
            buf.writeNbt(nbt);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            workstations.clear();

            var nbt = buf.readNbt();

            if (nbt != null) {
                var blockList = nbt.getList("workstations", 8);
                for (var blockTag : blockList) {
                    var workstation = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockTag.getAsString()));
                    workstations.add(workstation);
                }
            }
        }

        public List<Block> getWorkstations() {
            return workstations;
        }
    }
}
