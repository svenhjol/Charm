package svenhjol.charm.foundation.helper;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class ClientRegistryHelper {
    public static <T extends ItemLike> void itemTab(Supplier<T> item, ResourceKey<CreativeModeTab> key, @Nullable ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.addAfter(showAfter, item.get()));
        } else {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.accept(item.get()));
        }
    }
}
