package svenhjol.charm.api.iface;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.Supplier;

/**
 * Add the final blockstate for a crop-type block that can be harvested by Charm's QuickReplant feature.
 */
@SuppressWarnings("unused")
public interface IQuickReplantProvider {
    List<Supplier<BlockState>> getHarvestableBlocks();
}
