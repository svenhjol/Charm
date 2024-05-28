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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.api.enums.EventResult;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<Casks> {
    public Handlers(Casks feature) {
        super(feature);
    }

    public ItemStack getFilledWaterBottle() {
        return PotionContents.createItemStack(Items.POTION, Potions.WATER);
    }

    public boolean isValidPotion(ItemStack potion) {
        boolean valid = potion.is(Items.POTION);

        if (!valid && feature().allowSplashAndLingering()) {
            valid = potion.is(Items.LINGERING_POTION) || potion.is(Items.SPLASH_POTION);
        }

        return valid;
    }

    public PotionContents getPotion(ItemStack stack) {
        if (isValidPotion(stack)) {
            return stack.get(DataComponents.POTION_CONTENTS);
        }
        return PotionContents.EMPTY;
    }

    public ItemStack makeCustomPotion(List<MobEffectInstance> effects) {
        var stack = feature().handlers.getFilledWaterBottle();
        var basePotion = getPotion(stack);

        var newPotionContents = new PotionContents(basePotion.potion(), basePotion.customColor(), effects);
        stack.set(DataComponents.POTION_CONTENTS, newPotionContents);

        var customName = Component.translatable("item.charm.home_brew");
        stack.set(DataComponents.CUSTOM_NAME, customName);

        return stack;
    }

    public EventResult playerAddToCask(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
                                       InteractionHand hand, BlockHitResult hitResult) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CaskBlockEntity cask) {
            if (stack.getItem() == Items.NAME_TAG && stack.has(DataComponents.CUSTOM_NAME)) {

                // Name the cask using a name tag.
                cask.name = stack.getHoverName();
                cask.setChanged();

                level.playSound(null, pos, feature().registers.nameSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                stack.shrink(1);

            } else if (stack.is(Items.GLASS_BOTTLE)) {

                // Take a bottle of liquid from the cask using a glass bottle.
                var out = cask.take();
                if (out != null) {
                    player.getInventory().add(out);

                    stack.shrink(1);

                    if (cask.effects.size() > 1) {
                        feature().advancements.tookLiquidFromCask(player);
                    }
                }

            } else if (isValidPotion(stack)) {

                // Add a bottle of liquid to the cask using a filled glass bottle.
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

}
