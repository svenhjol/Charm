package svenhjol.charm.module.bundle_sorting;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CommonModule(mod = Charm.MOD_ID, description = "Scroll the mouse while hovering over a bundle to change the order of its contents.")
public class BundleSorting extends CharmModule {
    public static final String TAG_ITEMS = "Items"; // must match tag of BundleItem
    public static final ResourceLocation MSG_SERVER_CYCLE_BUNDLE_CONTENTS = new ResourceLocation(Charm.MOD_ID, "server_cycle_bundle_contents");

    @Override
    public void runWhenEnabled() {
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_CYCLE_BUNDLE_CONTENTS, this::handleCycleBundleContents);
    }

    private void handleCycleBundleContents(MinecraftServer server, ServerPlayer player, ServerGamePacketListener handler, FriendlyByteBuf buffer, PacketSender sender) {
        int slotIndex = buffer.readInt();
        boolean direction = buffer.readBoolean();

        server.execute(() -> {
            if (player == null) return;
            ItemStack bundle = player.containerMenu.getSlot(slotIndex).getItem();

            if (bundle.getItem() instanceof BundleItem) {
                List<ItemStack> contents = BundleItem.getContents(bundle).collect(Collectors.toCollection(LinkedList::new));
                if (contents.size() < 1) return;

                sortByScrollDirection(contents, direction);
                bundle.removeTagKey(TAG_ITEMS);

                CompoundTag nbt = bundle.getOrCreateTag();
                nbt.put(TAG_ITEMS, new ListTag());
                ListTag list = nbt.getList(TAG_ITEMS, 10);

                Collections.reverse(contents);
                contents.forEach(stack -> {
                    CompoundTag stackNbt = new CompoundTag();
                    stack.save(stackNbt);
                    list.add(0, stackNbt);
                });
            }
        });
    }

    public static void sortByScrollDirection(List<ItemStack> contents, boolean direction) {
        if (direction) {
            ItemStack last = contents.remove(contents.size() - 1);
            contents.add(0, last);
        } else {
            ItemStack first = contents.remove(0);
            contents.add(first);
        }
    }
}
