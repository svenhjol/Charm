package svenhjol.charm.module.cooking_pots;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.helper.ScreenRenderHelper;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

@ClientModule(module = CookingPots.class)
public class CookingPotsClient extends CharmModule {
    private final Map<List<ResourceLocation>, List<Item>> cachedItems = new WeakHashMap<>();

    @Override
    public void register() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
        ColorProviderRegistry.BLOCK.register(this::handleColorProvider, CookingPots.COOKING_POT);
        BlockEntityRendererRegistry.INSTANCE.register(CookingPots.BLOCK_ENTITY, CookingPotBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(CookingPots.MSG_CLIENT_ADDED_TO_POT, this::handleClientAddedToPot);
    }

    private int handleColorProvider(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        if (tintIndex == 1) {
            if (world != null) {
                if (state.getBlock() == CookingPots.COOKING_POT && state.getValue(CookingPotBlock.LIQUID) == 2) {
                    return 0x602A00;
                }
            }
            return 0x0088CC;
        }
        return -1;
    }

    private void handleRenderTooltip(PoseStack pose, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack == null || stack.getItem() != CookingPots.MIXED_STEW)
            return;

        List<ResourceLocation> contents = MixedStewItem.getContents(stack);
        if (contents.isEmpty())
            return;

        int hunger = (int)MixedStewItem.getHunger(stack);
        float saturation = MixedStewItem.getSaturation(stack);

        if (!cachedItems.containsKey(contents)) {
            this.cachedItems.clear();
            this.cachedItems.put(contents, CookingPots.getResolvedItems(contents));
        }

        List<Item> items = this.cachedItems.get(contents);
        if (items.isEmpty())
            return;

        final Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        boolean showTooltips = mc.options.advancedItemTooltips;

        pose.pushPose();
        RenderSystem.enableDepthTest();
        pose.translate(0, 0, 500);

        float oldZOffset = itemRenderer.blitOffset;
        itemRenderer.blitOffset = 500.0F; // hack to get front layer working

        int iconStartX = x + 12; // X value at which to start drawing icons
        int iconStartY = y - 14 + (lines.size() * 10); // Y value at which to start drawing icons

        // add blank lines so there is empty space for the icons
        for (int i = 0; i < 3; i++) {
            lines.add(ClientTooltipComponent.create(FormattedCharSequence.EMPTY));
        }

        // render the hunger icons
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int offsetStartX = 16; // 16 pixels from the left of the iconset
        int ox = 0;
        for (int i = 0; i < hunger; i++) {
            int iconOffsetX = i % 2 == 1 ? offsetStartX + 36 : offsetStartX + 45;
            ScreenRenderHelper.getIconRenderer().renderGuiIcon(pose, iconStartX + ox, iconStartY + 2, iconOffsetX, 27, 9, 9);
            if (i % 2 == 1) {
                ox += (hunger < 10 ? 8 : 5);
            }
        }

        // render the saturation icons
        RenderSystem.setShaderColor(0.68F, 0.8F, 0.0F, 1.0F);
        offsetStartX = 16; // 16 pixels from the left of the iconset
        ox = 0;
        for (int i = 1; i < (saturation * 10); i++) {
            int iconOffsetX = i % 2 == 1 ? offsetStartX + 54 : offsetStartX + 63;
            ScreenRenderHelper.getIconRenderer().renderGuiIcon(pose, iconStartX + ox, iconStartY + 11, iconOffsetX, 27, 9, 9);
            if (i % 2 == 1) {
                ox += (saturation < 1.2F ? 8 : 5);
            }
        }

        // render the item icons
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ox = 0;
        int showItems = showTooltips ? 10 : 7;
        int collapseAt = showTooltips ? 8 : 5;
        for (int i = 0; i < Math.min(showItems, items.size()); i++) {
            ItemStack itemStack = new ItemStack(items.get(i));
            if (!itemStack.isEmpty()) {
                itemRenderer.renderAndDecorateFakeItem(itemStack, iconStartX - 3 + (ox++ * (items.size() < collapseAt ? 13 : 7)), iconStartY + 19);
            }
        }

        itemRenderer.blitOffset = oldZOffset;
        RenderSystem.disableDepthTest();
        pose.popPose();
    }

    private void handleClientAddedToPot(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());
        client.execute(() -> {
            if (client.level != null)
                createParticles(client.level, pos);
        });
    }

    private void createParticles(Level world, BlockPos pos) {
        Random random = world.getRandom();

        for(int i = 0; i < 10; ++i) {
            double g = random.nextGaussian() * 0.02D;
            double h = random.nextGaussian() * 0.02D;
            double j = random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.13 + (0.73D * (double)random.nextFloat()), (double)pos.getY() + 0.8D + (double)random.nextFloat() * 0.3D, (double)pos.getZ() + 0.13D + (0.73 * (double)random.nextFloat()), g, h, j);
        }
    }
}
