package svenhjol.charm.module.ender_bundles;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.helper.TooltipHelper;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderBundlesClient extends CharmClientModule {
    public static float CACHED_AMOUNT_FILLED = 0.0F;

    public EnderBundlesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // set up scroll item model predicate
        ModelPredicateProviderRegistryAccessor.callRegister(new Identifier("ender_bundle_filled"), (stack, world, entity, i)
            -> EnderBundleItem.getAmountFilled());

        // register callbacks
        ClientPlayNetworking.registerGlobalReceiver(EnderBundles.MSG_CLIENT_UPDATE_ENDER_INVENTORY, this::handleClientUpdateEnderInventory);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    @Override
    public void init() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    /**
     * Handle message sent from the server containing updated ender inventory.
     */
    private void handleClientUpdateEnderInventory(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        NbtCompound nbt = data.readNbt();
        client.execute(() ->
            ClientHelper.getPlayer().ifPresent(player -> {
                if (nbt != null && nbt.contains("EnderItems", 9)) {
                    NbtList enderItems = nbt.getList("EnderItems", 10);
                    EnderChestInventory inventory = ((PlayerEntityAccessor) player).getEnderChestInventory();
                    inventory.readNbtList(enderItems);

                    CACHED_AMOUNT_FILLED = (float)enderItems.size() / inventory.size();
                }
            }));
    }

    /**
     * Poll for enderinventory changes on the server.
     */
    private void handleClientTick(MinecraftClient client) {
        if (client == null || client.world == null || client.player == null)
            return;

        // do this sparingly
        if (client.world.getTime() % 60 == 0) {
            ClientPlayNetworking.send(EnderBundles.MSG_SERVER_UPDATE_ENDER_INVENTORY, new PacketByteBuf(Unpooled.buffer()));
        }
    }

    /**
     * Poll for enderinventory changes on the server at a faster rate
     * when the player is hovering over an ender bundle.
     */
    private void handleRenderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<TooltipComponent> lines, int x, int y) {
        if (stack != null && stack.getItem() instanceof EnderBundleItem) {
            ClientHelper.getWorld().ifPresent(world -> {
                if (world.getTime() % 10 == 0)
                    ClientPlayNetworking.send(EnderBundles.MSG_SERVER_UPDATE_ENDER_INVENTORY, new PacketByteBuf(Unpooled.buffer()));
            });

            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<TooltipComponent> lines, int tx, int ty) {
        Optional<PlayerEntity> optional = ClientHelper.getPlayer();
        if (!optional.isPresent())
            return;

        PlayerEntity player = optional.get();
        EnderChestInventory inventory = player.getEnderChestInventory();
        int size = inventory.size();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            items.add(inventory.getStack(i));
        }

        if (items.stream().allMatch(ItemStack::isEmpty))
            return;

        TooltipHelper.renderOverlay(matrices, items, lines, tx, ty);
    }
}
