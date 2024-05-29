package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import svenhjol.charm.charmony.enums.EventResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.cooking_pots.CookingPots;

import java.util.Objects;

public final class Handlers extends FeatureHolder<CookingPots> {
    public static final int MAX_PORTIONS = 8;

    public Handlers(CookingPots feature) {
        super(feature);
    }

    public int getMaxPortions() {
        return MAX_PORTIONS;
    }

    public int getMaxHunger() {
        return feature().hungerPerStew() * MAX_PORTIONS;
    }

    public float getMaxSaturation() {
        return feature().saturationPerStew() * MAX_PORTIONS;
    }

    public ItemStack getFoodContainer(ItemStack input) {
        if (!isFood(input)) return ItemStack.EMPTY;

        var food = Objects.requireNonNull(input.get(DataComponents.FOOD));
        return food.usingConvertsTo().orElse(ItemStack.EMPTY);
    }

    public boolean isFull(BlockState state) {
        return state.getValue(CookingPotBlock.PORTIONS) == getMaxPortions();
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
                    player.getInventory().add(new ItemStack(Items.BUCKET));
                    stack.shrink(1);
                }

            } else if (isWaterBottle(stack) && pot.canAddWater()) {

                // Add a bottle of water. Increase portions by 1.
                var result = pot.fillWithBottle();
                if (result && !player.getAbilities().instabuild) {
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                    stack.shrink(1);
                }

            } else if (isFood(stack)) {

                // Add a food item to the pot.
                var result = pot.add(stack);
                if (result) {
                    if (!player.getAbilities().instabuild) {
                        // Handle things being given back to player e.g. bowl, bottle
                        var foodContainer = getFoodContainer(stack);
                        if (!foodContainer.isEmpty()) {
                            player.getInventory().add(foodContainer.copy());
                        }
                        stack.shrink(1);
                    }
                    feature().advancements.preparedCookingPot(player);
                }

            } else if (stack.is(Items.BOWL)) {

                // Take a bowl of stew from the pot using a wooden bowl.
                var out = pot.take();
                if (!out.isEmpty()) {
                    player.getInventory().add(out);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    feature().advancements.tookFoodFromCookingPot(player);
                }
            }

            return EventResult.SUCCESS;
        }

        return EventResult.PASS;
    }

    public void hopperAddToPot(CookingPotBlockEntity pot) {
        var input = pot.items.get(0);
        var output = pot.items.get(1);

        if (output.isEmpty()) {
            if (input.is(Items.BOWL)) {
                var out = pot.take();
                if (!out.isEmpty()) {
                    pot.items.set(1, out);
                    pot.setChanged();
                } else {
                    pot.items.set(1, input.copy());
                }
                input.shrink(1);

            } else if (isWaterBucket(input)) {

                if (pot.canAddWater()) {
                    pot.fillWithBucket();
                    pot.items.set(1, new ItemStack(Items.BUCKET));
                } else {
                    pot.items.set(1, input.copy());
                }
                input.shrink(1);

            } else if (isWaterBottle(input)) {

                if (pot.canAddWater()) {
                    pot.fillWithBucket();
                    pot.items.set(1, new ItemStack(Items.BUCKET));
                } else {
                    pot.items.set(1, input.copy());
                }
                input.shrink(1);

            } else if (isFood(input)) {

                if (pot.canAddFood() && pot.add(input)) {
                    var foodContainer = getFoodContainer(input);
                    if (!foodContainer.isEmpty()) {
                        pot.items.set(1, foodContainer.copy());
                    }
                } else {
                    pot.items.set(1, input.copy());
                }
                input.shrink(1);
            }
        }
    }
}
