package svenhjol.charm.feature.atlases;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.foundation.Registration;

public class CommonRegistration extends Registration<Atlases> {
    public CommonRegistration(Atlases feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        Atlases.atlasData = registry.dataComponent("charm_atlas_data", () -> builder -> builder.persistent(AtlasData.CODEC).networkSynchronized(AtlasData.STREAM_CODEC));
        Atlases.mapData = registry.dataComponent("charm_map_data", () -> builder -> builder.persistent(MapData.CODEC).networkSynchronized(MapData.STREAM_CODEC));
        Atlases.item = registry.item("atlas", AtlasItem::new);
        Atlases.menuType = registry.menuType("atlas", () -> new MenuType<>(AtlasContainer::new, FeatureFlags.VANILLA_SET));
        Atlases.openSound = registry.soundEvent("atlas_open");
        Atlases.closeSound = registry.soundEvent("atlas_close");
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerLogin(Player player) {
        if (!player.level().isClientSide()) {
            CommonNetworking.S2CSwappedAtlasSlot.send((ServerPlayer)player, -1);
        }
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide) {
            var serverPlayer = (ServerPlayer) player;

            for (var hand : InteractionHand.values()) {
                var held = serverPlayer.getItemInHand(hand);

                if (held.getItem() == Atlases.item.get()) {
                    var inventory = AtlasInventory.get(serverPlayer.level(), held);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        var slot = getSlotFromHand(serverPlayer, hand);
                        CommonNetworking.S2CUpdateInventory.send(serverPlayer, slot);

                        if (inventory.getMapInfos().size() >= Atlases.NUMBER_OF_MAPS_FOR_ACHIEVEMENT) {
                            Atlases.triggerMadeAtlasMaps(serverPlayer);
                        }
                    }
                }
            }
        }
    }

    public static int getSlotFromHand(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getInventory().selected;
        } else {
            return player.getInventory().getContainerSize() - 1;
        }
    }
}
