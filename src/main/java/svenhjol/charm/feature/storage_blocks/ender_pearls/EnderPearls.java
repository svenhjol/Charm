package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.event.EntityJoinEvent;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class EnderPearls implements IStorageBlockFeature {
    private static final String ID = "ender_pearl_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static boolean enabled;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.enderPearlsEnabled);
    }

    @Override
    public void register() {
        var registry = Mods.common(Charm.ID).registry();
        block = registry.block(ID, EnderPearlBlock::new);
        item = registry.item(ID, EnderPearlBlock.BlockItem::new);
        enabled = checks().stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
    }

    private void handleEntityJoin(Entity entity, Level level) {
        if (!StorageBlocks.enderPearlBlocksConvertSilverfish) {
            return;
        }

        // Must be a silverfish.
        if (!(entity instanceof Silverfish silverfish)) {
            return;
        }

        var goalSelector = silverfish.goalSelector;

        // Add the ender pearl block burrowing goal if it isn't already present in the silverfish AI.
        var hasGoal = goalSelector.getRunningGoals().anyMatch(g -> g.getGoal() instanceof FormEndermiteGoal);
        if (!hasGoal) {
            goalSelector.addGoal(2, new FormEndermiteGoal(silverfish));
        }
    }

    public static void triggerConvertedSilverfish(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(new ResourceLocation(Charm.ID, "converted_silverfish"), player));
    }
}
