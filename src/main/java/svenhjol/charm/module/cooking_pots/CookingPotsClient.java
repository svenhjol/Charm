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
import svenhjol.charm.loader.CharmClientModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

@ClientModule(module = CookingPots.class)
public class CookingPotsClient extends CharmClientModule {
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

    private void handleRenderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack == null || stack.getItem() != CookingPots.MIXED_STEW)
            return;

        List<ResourceLocation> contents = MixedStewItem.getContents(stack);
        if (contents.isEmpty())
            return;

        if (!cachedItems.containsKey(contents)) {
            this.cachedItems.clear();
            this.cachedItems.put(contents, CookingPots.getResolvedItems(contents));
        }

        List<Item> items = this.cachedItems.get(contents);
        if (items.isEmpty())
            return;

        final Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        matrices.pushPose();
        RenderSystem.enableDepthTest();
        matrices.translate(0, 0, 400);

        float oldZOffset = itemRenderer.blitOffset;
        itemRenderer.blitOffset = 400.0F; // hack to get front layer working

        // add a couple of blank lines below for the items to render into
        for (int i = 0; i < 2; i++) {
            lines.add(ClientTooltipComponent.create(FormattedCharSequence.EMPTY));
        }

        int ox = 0;
        for (int i = 0; i < Math.min(14, items.size()); i++) {
            ItemStack itemStack = new ItemStack(items.get(i));
            if (!itemStack.isEmpty())
                itemRenderer.renderAndDecorateFakeItem(itemStack, x + 8 + (ox++ * (items.size() < 7 ? 13 : 6)), y + (lines.size() * 6));
        }

        itemRenderer.blitOffset = oldZOffset;
        RenderSystem.disableDepthTest();
        matrices.popPose();
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
