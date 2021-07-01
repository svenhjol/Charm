package svenhjol.charm.module.ender_bundles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.helper.TooltipHelper;
import svenhjol.charm.loader.CharmClientModule;
import svenhjol.charm.mixin.accessor.PlayerAccessor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ClientModule(module = EnderBundles.class)
public class EnderBundlesClient extends CharmClientModule {
    public static float CACHED_AMOUNT_FILLED = 0.0F;

    @Override
    public void register() {
        // set up scroll item model predicate
        ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("ender_bundle_filled"), (stack, world, entity, i)
            -> EnderBundleItem.getAmountFilled());

        // register callbacks
        ClientPlayNetworking.registerGlobalReceiver(EnderBundles.MSG_CLIENT_UPDATE_ENDER_INVENTORY, this::handleClientUpdateEnderInventory);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    @Override
    public void run() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    /**
     * Handle message sent from the server containing updated ender inventory.
     */
    private void handleClientUpdateEnderInventory(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        CompoundTag nbt = data.readNbt();
        client.execute(() ->
            ClientHelper.getPlayer().ifPresent(player -> {
                if (nbt != null && nbt.contains("EnderItems", 9)) {
                    ListTag enderItems = nbt.getList("EnderItems", 10);
                    PlayerEnderChestContainer inventory = ((PlayerAccessor) player).getEnderChestInventory();
                    inventory.fromTag(enderItems);

                    CACHED_AMOUNT_FILLED = (float)enderItems.size() / inventory.getContainerSize();
                }
            }));
    }

    /**
     * Poll for enderinventory changes on the server.
     */
    private void handleClientTick(Minecraft client) {
        if (client == null || client.level == null || client.player == null)
            return;

        // do this sparingly
        if (client.level.getGameTime() % 60 == 0) {
            ClientPlayNetworking.send(EnderBundles.MSG_SERVER_UPDATE_ENDER_INVENTORY, new FriendlyByteBuf(Unpooled.buffer()));
        }
    }

    /**
     * Poll for enderinventory changes on the server at a faster rate
     * when the player is hovering over an ender bundle.
     */
    private void handleRenderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && stack.getItem() instanceof EnderBundleItem) {
            ClientHelper.getWorld().ifPresent(world -> {
                if (world.getGameTime() % 10 == 0)
                    ClientPlayNetworking.send(EnderBundles.MSG_SERVER_UPDATE_ENDER_INVENTORY, new FriendlyByteBuf(Unpooled.buffer()));
            });

            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int tx, int ty) {
        Optional<Player> optional = ClientHelper.getPlayer();
        if (!optional.isPresent())
            return;

        Player player = optional.get();
        PlayerEnderChestContainer inventory = player.getEnderChestInventory();
        int size = inventory.getContainerSize();
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            items.add(inventory.getItem(i));
        }

        if (items.stream().allMatch(ItemStack::isEmpty))
            return;

        TooltipHelper.renderOverlay(matrices, items, lines, tx, ty);
    }
}
