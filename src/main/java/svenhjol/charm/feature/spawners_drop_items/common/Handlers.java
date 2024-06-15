package svenhjol.charm.feature.spawners_drop_items.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.spawners_drop_items.SpawnersDropItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<SpawnersDropItems> {
    private static final int ITEMS_PER_STACK = 64; // This might change in a future minecraft version.

    public Handlers(SpawnersDropItems feature) {
        super(feature);
    }

    @SuppressWarnings("SameReturnValue")
    public boolean blockBreak(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
        if (level.isClientSide) return true;
        if (feature().onlyPeaceful() && level.getDifficulty() != Difficulty.PEACEFUL) return true;

        if (state.getBlock() == Blocks.SPAWNER && level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            var baseSpawner = spawner.getSpawner();
            var random = level.getRandom();
            var entity = baseSpawner.getOrCreateDisplayEntity(level, random, pos);
            if (entity == null) return true;

            var type = entity.getType();
            for (var dropType : feature().registers.dropTypes.rowKeySet()) {
                if (!type.is(dropType)) continue;

                feature().registers.dropTypes.row(dropType).forEach((item, amount) -> {
                    var stacks = getItemStacks(item, amount);
                    for (var stack : stacks) {
                        for (var i = 0; i < amount; i++) {
                            var singleStack = new ItemStack(stack.getItem());
                            Vec3 vec3 = Vec3.atLowerCornerWithOffset(pos, 0.5d, 0.5d, 0.5d).offsetRandom(level.random, 0.7f);
                            var itemEntity = new ItemEntity(level, vec3.x(), vec3.y(), vec3.z(), singleStack);
                            level.addFreshEntity(itemEntity);
                        }
                    }
                });
            }
        }

        return true;
    }

    private List<ItemStack> getItemStacks(Item item, int amount) {
        List<ItemStack> stacks = new ArrayList<>();
        int numberOfStacks = 1;
        int remaining = amount;

        if (amount > ITEMS_PER_STACK) {
            numberOfStacks = amount / ITEMS_PER_STACK;
        }

        for (int i = 0; i < numberOfStacks; i++) {
            var stack = new ItemStack(item);
            stack.setCount(Math.min(remaining, ITEMS_PER_STACK));
            stacks.add(stack);
            remaining -= ITEMS_PER_STACK;
        }

        return stacks;
    }
}
