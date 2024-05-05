package svenhjol.charm.feature.extractable_enchantments;

import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.api.event.GrindstoneEvents;
import svenhjol.charm.api.event.GrindstoneEvents.GrindstoneMenuInstance;
import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<ExtractableEnchantments> {
    public CommonRegistration(ExtractableEnchantments feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        GrindstoneEvents.ON_TAKE.handle(this::handleOnTake);
        GrindstoneEvents.CALCULATE_OUTPUT.handle(this::handleCalculateOutput);
        GrindstoneEvents.CAN_TAKE.handle(this::handleCanTake);
        GrindstoneEvents.CAN_PLACE.handle(this::handleCanPlace);
    }

    private InteractionResult handleCanTake(GrindstoneMenuInstance instance, Player player) {
        var stacks = CommonCallbacks.getStacksFromInventory(instance.output);

        if (CommonCallbacks.shouldExtract(stacks)) {
            var cost = CommonCallbacks.getEnchantedItemFromStacks(stacks)
                .map(CommonCallbacks::getCost)
                .orElse(0);

            if (CommonCallbacks.hasEnoughXp(player, cost)) {
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean handleCanPlace(Container container, ItemStack itemStack) {
        return itemStack.is(Items.BOOK);
    }

    private boolean handleOnTake(GrindstoneMenuInstance instance, Player player, ItemStack stack) {
        var out = CommonCallbacks.tryGetEnchantedBook(instance.input, player);
        if (out == null) return false;

        instance.access.execute((level, pos) -> {
            if (out.getItem() instanceof EnchantedBookItem) {
                if (!player.getAbilities().instabuild) {
                    int cost = CommonCallbacks.getCost(stack);
                    player.giveExperienceLevels(-cost);
                }
                ExtractableEnchantments.triggerExtractedEnchantment(player);
            }
            level.levelEvent(1042, pos, 0);
        });

        var slot0 = instance.input.getItem(0);
        var slot1 = instance.input.getItem(1);

        if (slot0.getCount() > 1) {
            slot0.shrink(1);
        } else if (slot1.getCount() > 1) {
            slot1.shrink(1);
        }

        if (slot0.getCount() <= 1) {
            instance.input.setItem(0, ItemStack.EMPTY);
        }

        if (slot1.getCount() <= 1) {
            instance.input.setItem(1, ItemStack.EMPTY);
        }

        instance.menu.broadcastChanges();
        return true;
    }

    private boolean handleCalculateOutput(GrindstoneEvents.GrindstoneMenuInstance instance) {
        var out = CommonCallbacks.tryGetEnchantedBook(instance.input, instance.player);
        if (out == null) return false;

        instance.output.setItem(0, out);
        return true;
    }
}
