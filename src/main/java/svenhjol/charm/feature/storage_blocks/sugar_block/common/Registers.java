package svenhjol.charm.feature.storage_blocks.sugar_block.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.api.event.SugarDissolveEvent;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlock;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<SugarBlock> {
    private static final String ID = "sugar_block";
    public final Supplier<Block> block;
    public final Supplier<Block.BlockItem> item;
    public final Supplier<SoundEvent> dissolveSound;

    public Registers(SugarBlock feature) {
        super(feature);

        block = feature().registry().block(ID, Block::new);
        item = feature().registry().item(ID, () -> new Block.BlockItem(block));
        dissolveSound = feature().registry().soundEvent("sugar_dissolve");
    }



    @Override
    public void onEnabled() {
        SugarDissolveEvent.INSTANCE.handle(feature().handlers::sugarDissolve, 0);
    }

}
