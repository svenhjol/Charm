package svenhjol.charm.feature.atlases.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.api.event.HeldItemRenderEvent;
import svenhjol.charm.api.event.KeyPressEvent;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.atlases.common.Networking;
import svenhjol.charm.feature.atlases.common.Networking.C2SSwapAtlasSlot;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AtlasesClient> {
    private final Supplier<String> openAtlasKey;
    private AtlasRenderer renderer;

    public Registers(AtlasesClient feature) {
        super(feature);

        feature().registry().menuScreen(feature().common().registers.menuType,
            () -> AtlasScreen::new);

        openAtlasKey = feature().registry().key("open_atlas",
            () -> new KeyMapping("key.charm.open_atlas", GLFW.GLFW_KEY_R, "key.categories.inventory"));

        // Client packet receivers
        feature().registry().packetReceiver(Networking.S2CSwappedAtlasSlot.TYPE,
            () -> feature().handlers::handleSwappedSlot);
        feature().registry().packetReceiver(Networking.S2CUpdateInventory.TYPE,
            () -> feature().handlers::handleUpdateInventory);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.MAP
        );

        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
        HeldItemRenderEvent.INSTANCE.handle(this::handleRenderHeldItem);
    }

    // TODO: move to client handlers
    private void handleKeyPress(String id) {
        if (Minecraft.getInstance().level != null && id.equals(openAtlasKey.get())) {
            C2SSwapAtlasSlot.send(feature().handlers.swappedSlot);
        }
    }

    // TODO: move to client handlers
    private InteractionResult handleRenderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (itemStack.getItem() == feature().common().registers.item.get()) {
            if (renderer == null) {
                renderer = new AtlasRenderer();
            }
            renderer.renderAtlas(poseStack, multiBufferSource, light, hand, equipProgress, swingProgress, itemStack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
