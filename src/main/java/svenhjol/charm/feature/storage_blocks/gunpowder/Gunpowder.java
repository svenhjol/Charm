package svenhjol.charm.feature.storage_blocks.gunpowder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.api.iface.IStorageBlockFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Gunpowder implements IStorageBlockFeature {
    private static final String ID = "gunpowder_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static Supplier<SoundEvent> dissolveSound;
    static boolean enabled;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        block = registry.block(ID, GunpowderBlock::new);
        item = registry.item(ID, GunpowderBlock.BlockItem::new);
        dissolveSound = registry.soundEvent("gunpowder_dissolve");
        enabled = checks().stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.gunpowderEnabled);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
