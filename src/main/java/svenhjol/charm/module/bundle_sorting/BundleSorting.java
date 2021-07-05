package svenhjol.charm.module.bundle_sorting;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.BundleItemAccessor;
import svenhjol.charm.module.ender_bundles.EnderBundles;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CommonModule(mod = Charm.MOD_ID, description = "Scroll the mouse while hovering over a bundle to change the order of its contents.")
public class BundleSorting extends CharmModule {
    public static final String ITEMS_NBT = "Items"; // must match tag of BundleItem
    public static final ResourceLocation MSG_SERVER_CYCLE_BUNDLE_CONTENTS = new ResourceLocation(Charm.MOD_ID, "server_cycle_bundle_contents");
    public static final int sensitivity = 2;

    @Override
    public void register() {
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_CYCLE_BUNDLE_CONTENTS, this::handleCycleBundleContents);
    }

    private void handleCycleBundleContents(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        int slotIndex = data.readInt();
        boolean direction = data.readBoolean();

        server.execute(() -> {
            if (player == null)
                return;

            ItemStack bundle = player.containerMenu.getSlot(slotIndex).getItem();

            if (bundle.getItem() instanceof BundleItem) {
                List<ItemStack> contents = BundleItemAccessor.invokeGetContents(bundle).collect(Collectors.toCollection(LinkedList::new));
                if (contents.size() < 1)
                    return;

                sortByScrollDirection(contents, direction);
                bundle.removeTagKey(ITEMS_NBT);

                CompoundTag nbt = bundle.getOrCreateTag();
                nbt.put(ITEMS_NBT, new ListTag());
                ListTag list = nbt.getList(ITEMS_NBT, 10);

                Collections.reverse(contents);
                contents.forEach(stack -> {
                    CompoundTag stackNbt = new CompoundTag();
                    stack.save(stackNbt);
                    list.add(0, stackNbt);
                });
            } else if (bundle.getItem() == EnderBundles.ENDER_BUNDLE) {
                PlayerEnderChestContainer enderChestInventory = player.getEnderChestInventory();
                List<ItemStack> contents = new LinkedList<>();

                for (int i = 0; i < enderChestInventory.getContainerSize(); i++) {
                    ItemStack stack = enderChestInventory.getItem(i);
                    if (!stack.isEmpty())
                        contents.add(stack);
                }

                sortByScrollDirection(contents, direction);
                enderChestInventory.clearContent();
                contents.forEach(enderChestInventory::addItem);
            }
        });
    }

    private void sortByScrollDirection(List<ItemStack> contents, boolean direction) {
        if (direction) {
            ItemStack last = contents.remove(contents.size() - 1);
            contents.add(0, last);
        } else {
            ItemStack first = contents.remove(0);
            contents.add(first);
        }
    }
}
