package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import svenhjol.charm.charmony.enums.EventResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.cooking_pots.CookingPots;

import java.util.Optional;

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
        return (FoodConstants.saturationByModifier(feature().stewHungerRestored(), feature().stewSaturationRestored()) * MAX_PORTIONS) / 4.0f;
    }

    public ItemStack getFoodContainer(ItemStack input) {
        var copy = input.copy();
        
        if (!isFood(copy)) {
            return ItemStack.EMPTY;
        }

        var food = copy.get(DataComponents.FOOD);
        if (food == null) {
            return ItemStack.EMPTY;
        }

        var opt = food.usingConvertsTo();
        return opt.map(ItemStack::copy).orElse(ItemStack.EMPTY);
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

    public EventResult playerAddToPot(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity pot) {
            if (isWaterBucket(stack) && pot.canAddWater()) {

                // Add a water bucket to the pot.
                if (!level.isClientSide()) {
                    if (pot.fillWithBucket() && !player.hasInfiniteMaterials()) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
                    }
                }
                return EventResult.SUCCESS;

            } else if (isWaterBottle(stack) && pot.canAddWater()) {

                // Add a bottle of water. Increase portions by 2.
                if (!level.isClientSide()) {
                    if (pot.fillWithBottle() && !player.hasInfiniteMaterials()) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    }
                }
                return EventResult.SUCCESS;

            } else if (stack.is(Items.BOWL)) {

                // Take a bowl of stew from the pot using a wooden bowl.
                if (!level.isClientSide()) {
                    var out = pot.take();
                    if (out != null) {
                        if (!player.hasInfiniteMaterials()) {
                            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, out));
                        } else {
                            // Creative mode
                            player.getInventory().add(out);
                        }
                        feature().advancements.tookFoodFromCookingPot(player);
                    }
                }
                return EventResult.SUCCESS;
            }
        }

        return EventResult.PASS;
    }
    
    public Optional<ItemStack> dispenserTakeFromPot(CookingPotBlockEntity pot) {
        return Optional.ofNullable(pot.take());
    }
    
    public boolean dispenserAddToPot(CookingPotBlockEntity pot, ItemStack stack) {
        var level = pot.getLevel();
        var pos = pot.getBlockPos();
        var center = pos.getCenter();
        
        if (level == null) {
            return false;
        }
        
        boolean result = false;
        ItemStack drop = null;
        if (isWaterBucket(stack) && pot.fillWithBucket()) {
            drop = new ItemStack(Items.BUCKET);
            result = true;
        } else if (isWaterBottle(stack) && pot.fillWithBottle()) {
            drop = new ItemStack(Items.GLASS_BOTTLE);
            result = true;
        }
        if (drop != null) {
            level.addFreshEntity(new ItemEntity(level, center.x(), center.y(), center.z(), drop));
        }
        
        return result;
    }

    public void entityInside(Level level, BlockPos pos, BlockState state, Entity entity, CookingPotBlockEntity pot) {
        var center = pos.getCenter();
        
        if (entity instanceof ItemEntity itemEntity && !level.isClientSide()) {
            var stack = itemEntity.getItem();
            if (isFood(stack) && pot.canAddFood()) {
                var result = pot.add(stack);
                if (result) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25f, 1.0f);

                    // Do prepare cooking pot advancement for all nearby players.
                    PlayerHelper.getPlayersInRange(level, pos, 8.0d)
                        .forEach(player -> feature().advancements.preparedCookingPot(player));

                    // Handle containers e.g. bowl, bottle. They sit in the pot until taken out by player or hopper.
                    var foodContainer = getFoodContainer(stack).copy();
                    log().dev("Food container item: " + foodContainer);
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
