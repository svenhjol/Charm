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
        feature().registry().clientPacketReceiver(new Networking.S2CSwappedAtlasSlot(),
            () -> feature().handlers::swappedSlotReceived);
        feature().registry().clientPacketReceiver(new Networking.S2CUpdateInventory(),
            () -> feature().handlers::updateInventoryReceived);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.item.get(),
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.MAP
        );

        KeyPressEvent.INSTANCE.handle(feature().handlers::keyPress);
        HeldItemRenderEvent.INSTANCE.handle(feature().handlers::renderHeldItem);
    }
}
