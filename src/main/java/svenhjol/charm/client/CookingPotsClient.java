package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.block.CookingPotBlock;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.item.MixedStewItem;
import svenhjol.charm.module.CookingPots;
import svenhjol.charm.render.CookingPotBlockEntityRenderer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

@Environment(EnvType.CLIENT)
public class CookingPotsClient extends CharmClientModule {
    private final Map<List<Identifier>, List<Item>> cachedItems = new WeakHashMap<>();

    public CookingPotsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
        ColorProviderRegistry.BLOCK.register(this::handleColorProvider, CookingPots.COOKING_POT);
        BlockEntityRendererRegistry.INSTANCE.register(CookingPots.BLOCK_ENTITY, CookingPotBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(CookingPots.MSG_CLIENT_ADDED_TO_POT, this::handleClientAddedToPot);
    }

    private int handleColorProvider(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null) {
            if (state.getBlock() == CookingPots.COOKING_POT && state.get(CookingPotBlock.LIQUID) == 2) {
                return 0x804300;
            }
        }
        return 0x0088CC;
    }

    private void handleRenderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<TooltipComponent> lines, int x, int y) {
        if (stack == null || stack.getItem() != CookingPots.MIXED_STEW)
            return;

        List<Identifier> contents = MixedStewItem.getContents(stack);
        if (contents.isEmpty())
            return;

        if (!cachedItems.containsKey(contents)) {
            this.cachedItems.clear();
            this.cachedItems.put(contents, CookingPots.getResolvedItems(contents));
        }

        List<Item> items = this.cachedItems.get(contents);
        if (items.isEmpty())
            return;

        final MinecraftClient mc = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        matrices.push();
        RenderSystem.enableDepthTest();
        matrices.translate(0, 0, 400);

        float oldZOffset = itemRenderer.zOffset;
        itemRenderer.zOffset = 400.0F; // hack to get front layer working

        int ox = 0;
        for (int i = 0; i < Math.min(14, items.size()); i++) {
            ItemStack itemStack = new ItemStack(items.get(i));
            if (!itemStack.isEmpty()) {
                itemRenderer.renderInGui(itemStack, x + 8 + (ox++ * (items.size() < 7 ? 13 : 6)), y + 22);
            }
        }

        // add a couple of blank lines below for the items to render into
        for (int i = 0; i < 2; i++) {
            lines.add(TooltipComponent.of(OrderedText.EMPTY));
        }

        itemRenderer.zOffset = oldZOffset;
        RenderSystem.disableDepthTest();
        matrices.pop();
    }

    private void handleClientAddedToPot(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        client.execute(() -> effectAddItem(pos));
    }

    private void effectAddItem(BlockPos pos) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null)
            return;

        Random random = world.getRandom();

        for(int i = 0; i < 10; ++i) {
            double g = random.nextGaussian() * 0.02D;
            double h = random.nextGaussian() * 0.02D;
            double j = random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.COMPOSTER, (double)pos.getX() + 0.13 + (0.73D * (double)random.nextFloat()), (double)pos.getY() + 0.8D + (double)random.nextFloat() * 0.3D, (double)pos.getZ() + 0.13D + (0.73 * (double)random.nextFloat()), g, h, j);
        }
    }
}
