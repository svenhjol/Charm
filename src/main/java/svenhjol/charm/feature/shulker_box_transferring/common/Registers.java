package svenhjol.charm.feature.shulker_box_transferring.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.event.ItemDragDropEvent;
import svenhjol.charm.feature.shulker_box_transferring.ShulkerBoxTransferring;
import svenhjol.charm.foundation.feature.RegisterHolder;
import svenhjol.charm.foundation.helper.TagHelper;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<ShulkerBoxTransferring> {
    public final List<ItemLike> blacklist = new ArrayList<>();

    public Registers(ShulkerBoxTransferring feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ItemDragDropEvent.INSTANCE.handle(feature().handlers::itemDragDrop);
    }

    @Override
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        for (var block : TagHelper.getValues(BuiltInRegistries.BLOCK, BlockTags.SHULKER_BOXES)) {
            if (!blacklist.contains(block)) {
                blacklist.add(block);
            }
        }
    }
}
