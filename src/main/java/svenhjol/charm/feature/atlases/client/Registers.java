package svenhjol.charm.feature.atlases.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.charmony.event.HeldItemRenderEvent;
import svenhjol.charm.charmony.event.KeyPressEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.atlases.common.Networking;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AtlasesClient> {
    public final Supplier<String> openAtlasKey;

    public Registers(AtlasesClient feature) {
        super(feature);

        feature().registry().menuScreen(feature().linked().registers.menuType,
            () -> AtlasScreen::new);

        openAtlasKey = feature().registry().key("open_atlas",
            () -> new KeyMapping("key.charm.open_atlas", GLFW.GLFW_KEY_R, "key.categories.inventory"));

        // Client packet receivers
        feature().registry().packetReceiver(Networking.S2CSwappedAtlasSlot.TYPE,
            () -> feature().handlers::swappedSlotReceived);
        feature().registry().packetReceiver(Networking.S2CUpdateInventory.TYPE,
            () -> feature().handlers::updateInventoryReceived);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.MAP
        );

        KeyPressEvent.INSTANCE.handle(feature().handlers::keyPress);
        HeldItemRenderEvent.INSTANCE.handle(feature().handlers::renderHeldItem);
    }
}
