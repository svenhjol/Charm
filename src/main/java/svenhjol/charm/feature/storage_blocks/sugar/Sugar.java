package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony.iface.ICommonRegistry;
import svenhjol.charmony_api.enums.EventResult;
import svenhjol.charmony_api.event.SugarDissolveEvent;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Sugar implements IStorageBlockFeature<ICommonRegistry> {
    private static final String ID = "sugar_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static Supplier<SoundEvent> dissolveSound;
    static boolean enabled;
    ICommonRegistry registry;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.sugarEnabled);
    }

    @Override
    public void preRegister(ICommonRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void register() {
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
    public void runWhenEnabled() {
        SugarDissolveEvent.INSTANCE.handle(this::handleSugarDissolve, 0);
    }

    private EventResult handleSugarDissolve(Level level, BlockPos pos) {
        level.removeBlock(pos, true);
        level.playSound(null, pos, Sugar.dissolveSound.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        return EventResult.PASS;
    }

    static void triggerDissolvedSugar(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(new ResourceLocation(Charm.ID, "dissolved_sugar"), player));
    }
}
