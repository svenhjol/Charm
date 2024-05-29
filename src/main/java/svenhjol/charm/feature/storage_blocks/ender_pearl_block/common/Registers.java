package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import net.minecraft.world.item.Item;
import svenhjol.charm.charmony.event.EntityJoinEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<EnderPearlBlock> {
    private static final String ID = "ender_pearl_block";
    public final Supplier<Block> block;
    public final Supplier<Item> item;
    public boolean enabled;

    public Registers(EnderPearlBlock feature) {
        super(feature);

        block = feature.registry().block(ID, Block::new);
        item = feature.registry().item(ID, () -> new Block.BlockItem(block));
    }

    @Override
    public void onEnabled() {
        EntityJoinEvent.INSTANCE.handle(feature().handlers::handleEntityJoin);
    }
}
