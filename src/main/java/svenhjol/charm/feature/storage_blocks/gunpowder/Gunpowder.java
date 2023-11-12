package svenhjol.charm.feature.storage_blocks.gunpowder;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Gunpowder implements IStorageBlockFeature<ICommonRegistry> {
    private static final String ID = "gunpowder_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static Supplier<SoundEvent> dissolveSound;
    static boolean enabled;
    ICommonRegistry registry;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.gunpowderEnabled);
    }

    @Override
    public void preRegister(ICommonRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void register() {
        block = registry.block(ID, GunpowderBlock::new);
        item = registry.item(ID, GunpowderBlock.BlockItem::new);
        dissolveSound = registry.soundEvent("gunpowder_dissolve");
        enabled = checks().stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    static void triggerDissolvedGunpowder(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(player
            -> Advancements.trigger(new ResourceLocation(Charm.ID, "dissolved_gunpowder"), player));
    }
}
