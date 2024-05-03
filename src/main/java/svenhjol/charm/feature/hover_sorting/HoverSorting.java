package svenhjol.charm.feature.hover_sorting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.api.iface.IHoverSortableItemProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.feature.hover_sorting.CommonNetworking.C2SScrollOnHover;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.Optional;

public class HoverSorting extends CommonFeature implements IHoverSortableItemProvider {
    @Override
    public String description() {
        return "Scroll the mouse while hovering over a bundle or shulker box to cycle the order of its contents.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        CharmApi.registerProvider(this);
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new CommonNetworking(this));
    }

    /**
     * Network callback from the client scroll event.
     * @see C2SScrollOnHover
     */
    public static void handleScrollOnHover(Player player, C2SScrollOnHover packet) {
        var container = player.containerMenu;
        var slotIndex = packet.slotIndex();
        var direction = packet.sortDirection();

        // Check that the slot index is not out of bounds.
        if (slotIndex >= container.slots.size() || slotIndex < 0) return;

        var itemInSlot = container.getSlot(slotIndex).getItem();

        // Invoke the hover sort event on the hovered item.
        ItemHoverSortEvent.INSTANCE.invoke((ServerPlayer)player, itemInSlot, direction);
    }

    @Override
    public List<ItemLike> getHoverSortableItems() {
        return List.of(Items.BUNDLE);
    }

    @Override
    public List<TagKey<Block>> getHoverSortableBlockTags() {
        return List.of(BlockTags.SHULKER_BOXES);
    }

    public static void triggerSortedItems(Player player) {
        Advancements.trigger(Charm.id("sorted_items"), player);
    }
}
