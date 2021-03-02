package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.OrderedText;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.handler.TooltipInventoryHandler;
import svenhjol.charm.item.EnderBundleItem;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.module.EnderBundles;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderBundlesClient extends CharmClientModule {
    public EnderBundlesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // set up scroll item model predicate
//        ModelPredicateProviderRegistryAccessor.callRegister(new Identifier("filled"), (stack, world, entity, i)
//            -> EnderBundleItem.getAmountFilled(entity, stack));

        ClientSidePacketRegistry.INSTANCE.register(EnderBundles.MSG_CLIENT_UPDATE_ENDER_INVENTORY, (context, data) -> {
            CompoundTag tag = data.readCompoundTag();
            context.getTaskQueue().execute(() ->
                ClientHelper.getPlayer().ifPresent(player -> {
                    if (tag != null && tag.contains("EnderItems", 9))
                        ((PlayerEntityAccessor)player).getEnderChestInventory().readTags(tag.getList("EnderItems", 10));
                }));
        });
    }

    @Override
    public void init() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    private void handleRenderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<? extends OrderedText> lines, int x, int y) {
        if (stack != null && stack.getItem() instanceof EnderBundleItem) {
            ClientHelper.getWorld().ifPresent(world -> {
                if (world.getTime() % 10 == 0)
                    ClientSidePacketRegistry.INSTANCE.sendToServer(EnderBundles.MSG_SERVER_UPDATE_ENDER_INVENTORY, new PacketByteBuf(Unpooled.buffer()));
            });

            renderTooltip(matrices, stack, lines, x, y);
        }
    }

    private void renderTooltip(MatrixStack matrices, @Nullable ItemStack stack, List<? extends OrderedText> lines, int tx, int ty) {
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

        TooltipInventoryHandler.renderOverlay(matrices, items, lines, tx, ty);
    }
}
