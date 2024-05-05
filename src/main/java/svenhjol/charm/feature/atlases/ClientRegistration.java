package svenhjol.charm.feature.atlases;

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
import svenhjol.charm.feature.atlases.CommonNetworking.C2SSwapAtlasSlot;
import svenhjol.charm.foundation.feature.Register;

public final class ClientRegistration extends Register<AtlasesClient> {
    private AtlasRenderer renderer;

    public ClientRegistration(AtlasesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        registry.menuScreen(Atlases.menuType, () -> AtlasScreen::new);

        AtlasesClient.openAtlasKey = registry.key("open_atlas",
            () -> new KeyMapping("key.charm.open_atlas", GLFW.GLFW_KEY_R, "key.categories.inventory"));
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(
            Atlases.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.MAP
        );

        KeyPressEvent.INSTANCE.handle(this::handleKeyPress);
        HeldItemRenderEvent.INSTANCE.handle(this::handleRenderHeldItem);
    }

    private void handleKeyPress(String id) {
        if (Minecraft.getInstance().level != null && id.equals(AtlasesClient.openAtlasKey.get())) {
            C2SSwapAtlasSlot.send(AtlasesClient.swappedSlot);
        }
    }

    private InteractionResult handleRenderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (itemStack.getItem() == Atlases.item.get()) {
            if (renderer == null) {
                renderer = new AtlasRenderer();
            }
            renderer.renderAtlas(poseStack, multiBufferSource, light, hand, equipProgress, swingProgress, itemStack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
