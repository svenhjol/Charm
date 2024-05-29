package svenhjol.charm.feature.storage_blocks.gunpowder_block.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<GunpowderBlock> {
    private static final String ID = "gunpowder_block";

    public final Supplier<Block> block;
    public final Supplier<BlockItem> item;
    public final Supplier<SoundEvent> dissolveSound;

    public Registers(GunpowderBlock feature) {
        super(feature);

        block = feature().registry().block(ID, Block::new);
        item = feature().registry().item(ID, () -> new Block.BlockItem(block));
        dissolveSound = feature().registry().soundEvent("gunpowder_dissolve");
    }
}
