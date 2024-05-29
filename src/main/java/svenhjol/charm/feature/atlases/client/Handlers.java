package svenhjol.charm.feature.atlases.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.atlases.common.AtlasInventory;
import svenhjol.charm.feature.atlases.common.Networking;
import svenhjol.charm.mixin.feature.atlases.CartographyTableScreenMixin;

public final class Handlers extends FeatureHolder<AtlasesClient> {
    private AtlasRenderer renderer;
    private int swappedSlot = -1;

    public Handlers(AtlasesClient feature) {
        super(feature);
    }

    /**
     * Callback from {@link CartographyTableScreenMixin} to check
     * if the cartography table contains a map or an atlas.
     * @param screen The cartography table screen.
     * @return True if the cartography table contains a map or an atlas.
     */
    public boolean shouldDrawAtlasCopy(CartographyTableScreen screen) {
        return screen.getMenu().getSlot(0).getItem().getItem() == feature().linked().registers.item.get()
            && screen.getMenu().getSlot(1).getItem().getItem() == Items.MAP;
    }


    public void updateInventoryReceived(Player player, Networking.S2CUpdateInventory packet) {
        var slot = packet.slot();
        ItemStack atlas = player.getInventory().getItem(slot);
        AtlasInventory.get(player.level(), atlas).reload(atlas);
    }

    @SuppressWarnings("unused")
    public void swappedSlotReceived(Player player, Networking.S2CSwappedAtlasSlot packet) {
        swappedSlot = packet.slot();
    }

    public void keyPress(String id) {
        if (Minecraft.getInstance().level != null && id.equals(feature().registers.openAtlasKey.get())) {
            Networking.C2SSwapAtlasSlot.send(feature().handlers.swappedSlot);
        }
    }

    public InteractionResult renderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress,
                                            ItemStack itemStack, float equipProgress, PoseStack poseStack,
                                            MultiBufferSource multiBufferSource, int light) {
        if (itemStack.getItem() == feature().linked().registers.item.get()) {
            if (renderer == null) {
                renderer = new AtlasRenderer();
            }
            renderer.renderAtlas(poseStack, multiBufferSource, light, hand, equipProgress, swingProgress, itemStack);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
