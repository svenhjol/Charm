package svenhjol.charm.feature.storage_blocks.blocks;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.IStorageBlockFeature;
import svenhjol.charmony.annotation.Configurable;

import java.util.function.Supplier;

public class Gunpowder implements IStorageBlockFeature {
    private static final String ID = "gunpowder_block";
    static Supplier<Block> BLOCK;
    static Supplier<Item> BLOCK_ITEM;
    static Supplier<SoundEvent> DISSOLVE_SOUND;

    @Configurable(name = "Dissolve in lava", description = "If true, gunpowder blocks will dissolve when touching lava.")
    public static boolean dissolve = true;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        BLOCK = registry.block(ID, GunpowderBlock::new);
        BLOCK_ITEM = registry.item(ID, GunpowderBlock.BlockItem::new);
        DISSOLVE_SOUND = registry.soundEvent("gunpowder_dissolve");
    }
}
