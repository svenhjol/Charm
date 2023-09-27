package svenhjol.charm.feature.proximity_workstations;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelector;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelectorScreen;
import svenhjol.charm.feature.proximity_workstations.client.SelectWorkstationScreen;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony_api.event.KeyPressEvent;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

@ClientFeature(mod = Charm.MOD_ID, canBeDisabled = false)
public class ProximityWorkstationsClient extends CharmFeature {
    public static Supplier<String> openWorkstationSelectorKey;
    public static ResourceLocation selectWorkstationScreenBackground;

    @Override
    public void register() {
        openWorkstationSelectorKey = CharmClient.instance().registry().key("open_workstation_selector",
            () -> new KeyMapping("key.charm.open_workstation_selector", GLFW.GLFW_KEY_V, "key.categories.inventory"));

        selectWorkstationScreenBackground = Charm.instance().makeId("textures/gui/workstation_selector.png");
    }

    @Override
    public void runWhenEnabled() {
        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
    }

    private void handleKeyPress(String id) {
        if (id.equals(openWorkstationSelectorKey.get())) {
            openWorkstationSelector();
        }
    }

    private void openWorkstationSelector() {
        OpenWorkstationSelector.send();
    }

    public static void handleOpenedSelectorScreen(OpenWorkstationSelectorScreen message, Player player) {
        Minecraft.getInstance().setScreen(
            new SelectWorkstationScreen(
                message.getWorkstations()));
    }
}
