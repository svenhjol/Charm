package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import svenhjol.charm.api.enums.EventResult;
import svenhjol.charm.feature.cooking_pots.CookingPots;
import svenhjol.charm.foundation.feature.FeatureHolder;

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

    public boolean isFull(BlockState state) {
        return state.getValue(CookingPotBlock.PORTIONS) == getMaxPortions();
    }

    public boolean isEmpty(BlockState state) {
        return state.getValue(CookingPotBlock.PORTIONS) <= 0;
    }

    public boolean isValidHeatSource(BlockState state) {
        var valid = state.is(Tags.COOKING_HEAT_SOURCES);

        if (valid && state.hasProperty(BlockStateProperties.LIT)) {
            return state.getValue(BlockStateProperties.LIT);
        }

        return valid;
    }

    public EventResult addItemToPot(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BlockEntity pot) {
            if (stack.is(Items.WATER_BUCKET) && !level.isClientSide() && pot.canAddWater()) {

                // Add a water bucket to the pot.
                state = state.setValue(CookingPotBlock.PORTIONS, feature().handlers.getMaxPortions());
                level.setBlock(pos, state, 2);
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (!player.getAbilities().instabuild) {
                    player.getInventory().add(new ItemStack(Items.BUCKET));
                    stack.shrink(1);
                }

            } else if (stack.is(Items.POTION)
                && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)
                && pot.canAddWater()) {

                // Add a bottle of water. Increase portions by 1.
                state = state.setValue(CookingPotBlock.PORTIONS, state.getValue(CookingPotBlock.PORTIONS) + 1);
                level.setBlock(pos, state, 2);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (!player.getAbilities().instabuild) {
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                    stack.shrink(1);
                }

            } else if (stack.has(DataComponents.FOOD)) {
                // We know this is not null but the IDE doesn't :)
                //noinspection DataFlowIssue
                var convertsTo = stack.get(DataComponents.FOOD).usingConvertsTo();

                // Add a food item to the pot.
                var result = pot.add(stack);
                if (result) {
                    if (!player.getAbilities().instabuild) {
                        // Handle things being given back to player e.g. bowl, bottle
                        convertsTo.ifPresent(remainder -> {
                            if (!remainder.isEmpty()) {
                                player.getInventory().add(remainder.copy());
                            }
                        });
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
}
