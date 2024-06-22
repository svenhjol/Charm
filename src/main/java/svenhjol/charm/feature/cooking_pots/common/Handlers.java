package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.charmony.enums.EventResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.cooking_pots.CookingPots;

import java.util.Objects;

public final class Handlers extends FeatureHolder<CookingPots> {
    public static final int MAX_PORTIONS = 6;

    public Handlers(CookingPots feature) {
        super(feature);
    }

    public int maxPortions() {
        return MAX_PORTIONS;
    }

    public int maxHunger() {
        return feature().stewHungerRestored() * MAX_PORTIONS;
    }

    public float maxSaturation() {
        return FoodConstants.saturationByModifier(feature().stewHungerRestored(), feature().stewSaturationRestored()) * MAX_PORTIONS;
    }

    public ItemStack getFoodContainer(ItemStack input) {
        if (!isFood(input)) return ItemStack.EMPTY;

        var food = Objects.requireNonNull(input.get(DataComponents.FOOD));
        return food.usingConvertsTo().orElse(ItemStack.EMPTY);
    }

    public boolean isFull(BlockState state) {
        return state.getValue(CookingPotBlock.PORTIONS) == maxPortions();
    }

    public boolean isEmpty(BlockState state) {
        return state.getValue(CookingPotBlock.PORTIONS) <= 0;
    }

    public boolean isFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD);
    }

    public boolean isWaterBucket(ItemStack stack) {
        return stack.is(Items.WATER_BUCKET);
    }

    public boolean isWaterBottle(ItemStack stack) {
        return stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
    }

    public boolean isValidHeatSource(BlockState state) {
        var valid = state.is(Tags.COOKING_HEAT_SOURCES);

        if (valid && state.hasProperty(BlockStateProperties.LIT)) {
            return state.getValue(BlockStateProperties.LIT);
        }

        return valid;
    }

    public EventResult playerAddToPot(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity pot) {
            if (isWaterBucket(stack) && !level.isClientSide() && pot.canAddWater()) {

                // Add a water bucket to the pot.
                var result = pot.fillWithBucket();
                if (result && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                    player.getInventory().add(new ItemStack(Items.BUCKET));
                }
                return EventResult.CONSUME;

            } else if (isWaterBottle(stack) && pot.canAddWater()) {

                // Add a bottle of water. Increase portions by 2.
                var result = pot.fillWithBottle();
                if (result && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                }
                return EventResult.CONSUME;

            } else if (stack.is(Items.BOWL)) {

                // Take a bowl of stew from the pot using a wooden bowl.
                var out = pot.take();
                if (!out.isEmpty()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    
                    player.getInventory().add(out);
                    feature().advancements.tookFoodFromCookingPot(player);
                }
                return EventResult.CONSUME;
            }
        }

        return EventResult.PASS;
    }

    public void checkForThrownItems(CookingPotBlockEntity pot) {
        var level = pot.getLevel();
        var pos = pot.getBlockPos();
        if (level == null) return;
        
        var center = pos.getCenter();
        
        var itemEntities = level.getEntitiesOfClass(ItemEntity.class, (new AABB(center, center)).inflate(0.17d));
        for (var itemEntity : itemEntities) {
            var stack = itemEntity.getItem();
            if (isFood(stack) && pot.canAddFood()) {
                var result = pot.add(stack);
                if (result) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    
                    // Do prepare cooking pot advancement for all nearby players.
                    PlayerHelper.getPlayersInRange(level, pos, 8.0d)
                        .forEach(player -> feature().advancements.preparedCookingPot(player));
                    
                    // Handle containers e.g. bowl, bottle. They sit in the pot until taken out by player or hopper.
                    var foodContainer = getFoodContainer(stack);
                    if (!foodContainer.isEmpty()) {
                        level.addFreshEntity(new ItemEntity(level, center.x(), center.y(), center.z(), foodContainer));
                    }
                    
                    stack.shrink(1);
                }
            }
            if (stack.isEmpty()) {
                itemEntity.discard();
            }
        }
    }
}
