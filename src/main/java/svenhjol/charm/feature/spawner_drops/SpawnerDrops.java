package svenhjol.charm.feature.spawner_drops;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.CharmTags;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.event.BlockBreakEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpawnerDrops extends CommonFeature {
    static final int ITEMS_PER_STACK = 64;
    static final Table<TagKey<EntityType<?>>, Item, Integer> DROP_TYPES = HashBasedTable.create();

    @Configurable(name = "Peaceful only", description = "Spawners only drop items when the game difficulty is set to peaceful.")
    public static boolean onlyPeaceful = true;

    @Override
    public String description() {
        return "Spawners drop mob-related items when broken.";
    }

    @Override
    public void register() {
        registerDropType(CharmTags.SPAWNER_DROPS_BLAZE_RODS, Items.BLAZE_ROD, 32);
        registerDropType(CharmTags.SPAWNER_DROPS_BONES, Items.BONE, 64);
        registerDropType(CharmTags.SPAWNER_DROPS_GUNPOWDER, Items.GUNPOWDER, 128);
        registerDropType(CharmTags.SPAWNER_DROPS_MAGMA_CREAM, Items.MAGMA_CREAM, 64);
        registerDropType(CharmTags.SPAWNER_DROPS_ROTTEN_FLESH, Items.ROTTEN_FLESH, 64);
        registerDropType(CharmTags.SPAWNER_DROPS_SLIME_BALLS, Items.SLIME_BALL, 128);
        registerDropType(CharmTags.SPAWNER_DROPS_SPIDER_EYES, Items.SPIDER_EYE, 64);
        registerDropType(CharmTags.SPAWNER_DROPS_STRING, Items.STRING, 64);
    }

    @Override
    public void runWhenEnabled() {
        BlockBreakEvent.INSTANCE.handle(this::handleBlockBreak);
    }

    public static void registerDropType(TagKey<EntityType<?>> entity, Item item, int amount) {
        DROP_TYPES.put(entity, item, amount);
    }

    @SuppressWarnings("SameReturnValue")
    private boolean handleBlockBreak(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
        if (level.isClientSide) return true;
        if (onlyPeaceful && level.getDifficulty() != Difficulty.PEACEFUL) return true;

        if (state.getBlock() == Blocks.SPAWNER && level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner) {
            var baseSpawner = spawner.getSpawner();
            var random = level.getRandom();
            var entity = baseSpawner.getOrCreateDisplayEntity(level, random, pos);
            if (entity == null) return true;

            var type = entity.getType();
            for (var dropType : DROP_TYPES.rowKeySet()) {
                if (!type.is(dropType)) continue;

                DROP_TYPES.row(dropType).forEach((item, amount) -> {
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
