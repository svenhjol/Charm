package svenhjol.charm.feature.atlases;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charm.api.CharmApi;
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

        Atlases.atlasData = DataComponents.register("charm_atlas_data", builder -> builder.)

        Atlases.ITEM = registry.item("atlas", AtlasItem::new);
        Atlases.MENU_TYPE = registry.menuType("atlas", () -> new MenuType<>(AtlasContainer::new, FeatureFlags.VANILLA_SET));
        Atlases.OPEN_SOUND = registry.soundEvent("atlas_open");
        Atlases.CLOSE_SOUND = registry.soundEvent("atlas_close");

        AtlasesNetwork.register();
        CharmApi.registerProvider(this);
    }

    @Override
    public void onEnabled() {
        // TODO: register these events in CommonEvents
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    private void handlePlayerLogin(Player player) {
        if (!player.level().isClientSide()) {
            AtlasesNetwork.SwappedAtlasSlot.send(player, -1);
        }
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide) {
            var serverPlayer = (ServerPlayer) player;

            for (var hand : InteractionHand.values()) {
                var held = serverPlayer.getItemInHand(hand);

                if (held.getItem() == Atlases.ITEM.get()) {
                    var inventory = AtlasInventory.get(serverPlayer.level(), held);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        var slot = getSlotFromHand(serverPlayer, hand);
                        AtlasesNetwork.UpdateInventory.send(player, slot);

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
