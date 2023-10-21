package svenhjol.charm.feature.proximity_workstations;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelector;
import svenhjol.charm.feature.proximity_workstations.ProximityWorkstationsNetwork.OpenWorkstationSelectorScreen;
import svenhjol.charm.feature.proximity_workstations.client.SelectWorkstationScreen;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.event.KeyPressEvent;

import java.util.function.Supplier;

public class ProximityWorkstationsClient extends ClientFeature {
    public static Supplier<String> openWorkstationSelectorKey;
    public static ResourceLocation selectWorkstationScreenBackground;

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ProximityWorkstations.class;
    }

    @Override
    public void register() {
        openWorkstationSelectorKey = mod().registry().key("open_workstation_selector",
            () -> new KeyMapping("key.charm.open_workstation_selector", GLFW.GLFW_KEY_V, "key.categories.inventory"));

        selectWorkstationScreenBackground = mod().id("textures/gui/workstation_selector.png");
    }

    @Override
    public void runWhenEnabled() {
        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
    }

    private void handleKeyPress(String id) {
        if (Minecraft.getInstance().level == null) return;
        if (id.equals(openWorkstationSelectorKey.get())) {
            openWorkstationSelector();
        }
    }

    private void openWorkstationSelector() {
        mod().log().debug(getClass(), "Sending OpenWorkstationSelector packet");
        OpenWorkstationSelector.send();
    }

    public static void handleOpenedSelectorScreen(OpenWorkstationSelectorScreen message, Player player) {
        Minecraft.getInstance().setScreen(
            new SelectWorkstationScreen(
                message.getWorkstations()));
    }
}
