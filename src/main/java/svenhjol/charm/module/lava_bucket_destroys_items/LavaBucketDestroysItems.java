package svenhjol.charm.module.lava_bucket_destroys_items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.StackItemOnItemCallback;
import svenhjol.charm.lib.Advancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.lava_bucket_destroys_items.network.ClientSendDestroyWithLavaBucket;
import svenhjol.charm.module.lava_bucket_destroys_items.network.ServerReceiveDestroyWithLavaBucket;

import javax.annotation.Nullable;

@CommonModule(mod = Charm.MOD_ID, description = "Drop an item onto a lava bucket item to destroy it.")
public class LavaBucketDestroysItems extends CharmModule {
    public static final ResourceLocation TRIGGER_DESTROYED_ITEM = new ResourceLocation(Charm.MOD_ID, "destroyed_item_with_lava_bucket");
    public static ServerReceiveDestroyWithLavaBucket RECEIVE_DESTROYED_WITH_LAVA_BUCKET;
    public static ClientSendDestroyWithLavaBucket SEND_DESTROYED_WITH_LAVA_BUCKET;

    @Override
    public void runWhenEnabled() {
        RECEIVE_DESTROYED_WITH_LAVA_BUCKET = new ServerReceiveDestroyWithLavaBucket();
        SEND_DESTROYED_WITH_LAVA_BUCKET = new ClientSendDestroyWithLavaBucket();

        StackItemOnItemCallback.EVENT.register(this::handleStackedItem);
    }

    private boolean handleStackedItem(StackItemOnItemCallback.Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess) {
        if (direction == StackItemOnItemCallback.Direction.STACKED_ON_OTHER
            && clickAction == ClickAction.SECONDARY
            && slot.allowModification(player)
            && dest.getItem() == Items.LAVA_BUCKET
        ) {
            source.shrink(source.getCount());

            if (player.level.isClientSide) {
                player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
                SEND_DESTROYED_WITH_LAVA_BUCKET.send();
            }

            return true;
        }
        return false;
    }

    public static void triggerDestroyedItem(ServerPlayer player) {
        Advancements.triggerActionPerformed(player, LavaBucketDestroysItems.TRIGGER_DESTROYED_ITEM);
    }
}
