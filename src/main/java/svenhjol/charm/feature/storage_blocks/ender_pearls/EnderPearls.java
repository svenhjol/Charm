package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony_api.event.EntityJoinEvent;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class EnderPearls implements IStorageBlockFeature {
    private static final int RANGE = 8;
    private static final String ID = "ender_pearl_block";
    static Supplier<Block> block;
    static Supplier<Item> item;
    static boolean enabled;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        block = registry.block(ID, EnderPearlBlock::new);
        item = registry.item(ID, EnderPearlBlock.BlockItem::new);
        enabled = checks().stream().allMatch(BooleanSupplier::getAsBoolean);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> StorageBlocks.enderPearlsEnabled);
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

    public static boolean tryChorusTeleport(LivingEntity entity, ItemStack stack) {
        if (!StorageBlocks.enderPearlBlocksAreTeleportLocation) {
            return false;
        }

        if (entity.level().isClientSide) {
            return false;
        }

        var pos = entity.blockPosition();
        var level = (ServerLevel)entity.level();
        Map<Double, BlockPos> candidates = new HashMap<>();

        // Find potential teleport locations around the player.
        BlockPos.betweenClosedStream(
            pos.offset(-RANGE, -RANGE, -RANGE),
            pos.offset(RANGE, RANGE, RANGE)
        ).forEach(p -> {
            var above = p.above(1);
            var state = level.getBlockState(p);

            // Must be an ender pearl block.
            if (!state.is(block.get())) return;

            // Can't teleport the player to the same position.
            if (above.equals(pos)) return;

            // Avoid suffocation.
            var state1 = level.getBlockState(p.above(1));
            var state2 = level.getBlockState(p.above(2));
            if (!state1.isAir() || !state2.isAir()) return;

            var dist = getDistanceSquared(pos, above);
            candidates.put(dist, above);
        });

        if (candidates.isEmpty()) return false;

        // Get a single target location from the possible candidates.
        var target = candidates.get(Collections.min(candidates.keySet()));
        if (target == null) return false;

        double x = target.getX() + 0.5D;
        double y = target.getY();
        double z = target.getZ() + 0.5D;

        var didTeleport = entity.randomTeleport(x, y, z, true); // True to show particle effect.
        if (!didTeleport) return false;

        var sound = SoundEvents.CHORUS_FRUIT_TELEPORT;
        level.playSound(null, x, y, z, sound, SoundSource.PLAYERS, 1.0F, 1.0F); // At old location
        entity.playSound(sound, 1.0F, 1.0F); // At new location

        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(Items.CHORUS_FRUIT, 20);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                // TODO: advancement.
            }
        }

        return true;
    }

    public static double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }
}
