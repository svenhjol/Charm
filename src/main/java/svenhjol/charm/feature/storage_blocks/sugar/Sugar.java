package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Sugar implements IStorageBlockFeature {
    private static final String ID = "sugar_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static Supplier<SoundEvent> dissolveSound;
    static boolean enabled;
    static final ResourceLocation TRIGGER_DISSOLVED_SUGAR = Charm.instance().makeId("dissolved_sugar");

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.sugarEnabled);
    }

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

    public static void triggerAdvancementForNearbyPlayers(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(TRIGGER_DISSOLVED_SUGAR, (ServerPlayer)player));
    }
}
