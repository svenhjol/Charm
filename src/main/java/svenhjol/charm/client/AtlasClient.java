package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.screenhandler.AtlasInventory;
import svenhjol.charm.event.RenderHeldItemCallback;
import svenhjol.charm.base.gui.CharmHandledScreen;
import svenhjol.charm.module.Atlas;
import svenhjol.charm.render.AtlasRenderer;

public class AtlasClient extends CharmClientModule {
    private final AtlasRenderer renderer;

    public AtlasClient(CharmModule module) {
        super(module);
        renderer = new AtlasRenderer(MinecraftClient.getInstance().getTextureManager());
    }

    @Override
    public void register() {
        ScreenRegistry.register(Atlas.CONTAINER, CharmHandledScreen.createFactory(2));
    }

    @Override
    public void init() {
        RenderHeldItemCallback.EVENT.register(this::handleRenderItem);
    }

    private ActionResult handleRenderItem(float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (stack.getItem() == Atlas.ATLAS_ITEM) {
            renderAtlas(matrices, vertexConsumers, light, hand, pitch, equipProgress, swingProgress, stack);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public void renderAtlas(MatrixStack matrixStack, VertexConsumerProvider buffers, int light, Hand hand, float pitch, float equip, float swing, ItemStack stack) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld world = minecraft.world;
        ClientPlayerEntity player = minecraft.player;
        if (player == null) return;
        AtlasInventory inventory = Atlas.getInventory(world, stack);

        matrixStack.push(); // needed so that parent renderer isn't affect by what we do here

        // copypasta from renderMapFirstPersonSide
        float e = hand == Hand.MAIN_HAND ? 1.0F : -1.0F;
        matrixStack.translate(e * 0.125F, -0.125D, 0.0D);

        // render player arm
        if (!player.isInvisible()) {
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(e * 10.0F));
            renderer.renderArm(player, matrixStack, buffers, light, swing, equip, hand);
            matrixStack.pop();
        }

        // transform page based on the hand it is held and render it
        matrixStack.push();
        renderer.transformPageForHand(matrixStack, buffers, light, swing, equip, hand);
        renderer.renderAtlas(player, inventory, matrixStack, buffers, light);
        matrixStack.pop();

        matrixStack.pop(); // close
    }

}

