package svenhjol.charm.api.iface;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.ItemLike;

import java.util.List;

/**
 * Specify items that can be ground into other items by Charm's GrindableArmor feature.
 */
@SuppressWarnings("unused")
public interface IGrindableItemProvider {
    /**
     * List of pairs, the input item should be on the left and the output item on the right.
     * Example: List.of(Pair.of(Items.SADDLE, Items.LEATHER));
     */
    List<Pair<ItemLike, ItemLike>> getItemGrindResults();
}
