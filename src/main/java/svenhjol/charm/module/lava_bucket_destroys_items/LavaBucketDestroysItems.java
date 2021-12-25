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
import svenhjol.charm.api.event.StackItemOnItemCallback.Direction;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;

@CommonModule(mod = Charm.MOD_ID, description = "Drop an item onto a lava bucket item to destroy it.")
public class LavaBucketDestroysItems extends CharmModule {
    public static final ResourceLocation TRIGGER_DESTROYED_ITEM = new ResourceLocation(Charm.MOD_ID, "destroyed_item_with_lava_bucket");

    @Override
    public void runWhenEnabled() {
        StackItemOnItemCallback.EVENT.register(this::handleStackedItem);
    }

    private boolean handleStackedItem(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess) {
        if (direction == Direction.STACKED_ON_OTHER
            && clickAction == ClickAction.SECONDARY
            && slot.allowModification(player)
            && dest.getItem() == Items.LAVA_BUCKET
        ) {
            source.shrink(source.getCount());

            if (player.level.isClientSide) {
                player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
            } else {
                triggerDestroyedItem((ServerPlayer)player);
            }

            return true;
        }
        return false;
    }

    public static void triggerDestroyedItem(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_DESTROYED_ITEM);
    }
}
