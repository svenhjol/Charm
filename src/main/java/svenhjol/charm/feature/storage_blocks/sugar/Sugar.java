package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmapi.iface.IStorageBlockFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Sugar implements IStorageBlockFeature {
    private static final String ID = "sugar_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static Supplier<SoundEvent> dissolveSound;
    static boolean enabled;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        block = registry.block(ID, SugarBlock::new);
        item = registry.item(ID, SugarBlock.BlockItem::new);
        dissolveSound = registry.soundEvent("sugar_dissolve");
        enabled = checks().stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.sugarEnabled);
    }
}
