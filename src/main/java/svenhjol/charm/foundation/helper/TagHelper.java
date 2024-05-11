package svenhjol.charm.foundation.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

import java.util.LinkedList;
import java.util.List;

public final class TagHelper {
    public static <T> List<T> getValues(Registry<T> registry, TagKey<T> tags) {
        List<T> items = new LinkedList<>();

        var iter = registry.getTagOrEmpty(tags);
        for (Holder<T> holder : iter) {
            items.add(holder.value());
        }

        return items;
    }
}
