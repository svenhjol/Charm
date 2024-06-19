package svenhjol.charm.feature.casks.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.charmony.enums.EventResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.casks.Casks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class Handlers extends FeatureHolder<Casks> {
    public Handlers(Casks feature) {
        super(feature);
    }

    public ItemStack getBasePotionBottle() {
        var out = new ItemStack(Items.POTION);
        PotionUtils.setPotion(out, Potions.AWKWARD);
        return out;
    }

    public ItemStack getFilledWaterBottle() {
        var out = new ItemStack(Items.POTION);
        PotionUtils.setPotion(out, Potions.WATER);
        return out;
    }

    public boolean isValidPotion(ItemStack potion) {
        boolean valid = potion.is(Items.POTION);

        if (!valid && feature().allowSplashAndLingering()) {
            valid = potion.is(Items.LINGERING_POTION) || potion.is(Items.SPLASH_POTION);
        }

        return valid;
    }

    @Nullable
    public Potion getPotion(ItemStack stack) {
        if (isValidPotion(stack)) {
            return PotionUtils.getPotion(stack);
        }
        return null;
    }

    public ItemStack makeCustomPotion(Component customName, List<MobEffectInstance> effects, double fermentation) {
        var stack = feature().handlers.getBasePotionBottle();
        var basePotion = getPotion(stack);

        var newPotionContents = new PotionContents(basePotion.potion(), basePotion.customColor(), effects);
        stack.set(DataComponents.POTION_CONTENTS, newPotionContents);
        
        stack.setHoverName(customName);
        
        var homeBrewData = new HomeBrewData(fermentation);
        homeBrewData.set(stack);

        return stack;
    }

    public EventResult playerAddToCask(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                       InteractionHand hand, BlockHitResult hitResult) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity cask) {
            if (stack.getItem() == Items.NAME_TAG && stack.hasCustomHoverName()) {

                if (!level.isClientSide()) {
                    // Name the cask using a name tag.
                    cask.name = stack.getHoverName();
                    cask.setChanged();

                    level.playSound(null, pos, feature().registers.nameSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    stack.shrink(1);
                }
                
                return EventResult.SUCCESS;

            } else if (stack.is(Items.GLASS_BOTTLE)) {

                // Take a bottle of liquid from the cask using a glass bottle.
                if (!level.isClientSide()) {
                    var out = cask.take();
                    if (out != null) {
                        player.getInventory().add(out);

                        stack.shrink(1);

                        if (cask.effects.size() > 1) {
                            feature().advancements.tookLiquidFromCask(player);
                        }
                    }
                }
                return EventResult.SUCCESS;

            } else if (isValidPotion(stack)) {

                // Add a bottle of liquid to the cask using a filled glass bottle.
                if (!level.isClientSide()) {
                    var result = cask.add(stack);
                    if (result) {
                        stack.shrink(1);

                        // give the glass bottle back to the player
                        player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));

                        if (!level.isClientSide()) {
                            // Let nearby players know an item was added to the cask
                            Networking.S2CAddedToCask.send((ServerLevel) level, pos);

                            // do advancement for filling with potions
                            if (cask.bottles > 1 && cask.effects.size() > 1) {
                                feature().advancements.addedLiquidToCask(player);
                            }
                        }
                    }
                }
                return EventResult.SUCCESS;
            }
        }

        return EventResult.PASS;
    }

    public void hopperAddToCask(CaskBlockEntity cask) {
        var input = cask.items.get(0);
        var output = cask.items.get(1);

        if (output.isEmpty()) {
            if (input.is(Items.GLASS_BOTTLE)) {
                var out = cask.take();
                if (out != null) {
                    cask.items.set(1, out);
                    cask.setChanged();
                } else {
                    cask.items.set(1, new ItemStack(Items.GLASS_BOTTLE));
                }
                input.shrink(1);

            } else if (isValidPotion(input)) {

                var result = cask.add(input);
                if (result) {
                    cask.items.set(1, new ItemStack(Items.GLASS_BOTTLE));
                    cask.setChanged();
                } else {
                    cask.items.set(1, input);
                }
                input.shrink(1);
            }
        }
    }

    public Optional<ItemStack> restoreCustomPotionEffects(ItemStack original, ItemStack result) {
        var homeBrewData = HomeBrewData.EMPTY;
        
        if (!homeBrewData.has(original)) {
            // Do vanilla behavior if the original potion is not a home brew (from the cask).
            return Optional.empty();
        }
        
        var originalBrewData = homeBrewData.get(original);
        
        var originalCustomEffects = PotionUtils.getCustomEffects(original);
        var mixedCustomEffects = PotionUtils.getCustomEffects(result);
        
        if (mixedCustomEffects.isEmpty() && !originalCustomEffects.isEmpty()) {
            var copy = result.copy();
            PotionUtils.setCustomEffects(copy, originalCustomEffects);
            
            if (original.hasCustomHoverName()) {
                copy.setHoverName(original.getHoverName());
            }
            
            // Set the "home brew" data to the result so that we can track that it's a custom cask potion.
            originalBrewData.set(copy);
            return Optional.of(copy);
        }
        
        return Optional.empty();
    }
}
